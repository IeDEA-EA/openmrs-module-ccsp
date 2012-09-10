package org.openmrs.module.ccsp

import java.io.FileInputStream
import java.util.Date
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openmrs.api.context.Context
import org.openmrs.Encounter
import org.openmrs.Obs


def dumpWorkbookInfo = { wb ->
  numSheets = wb.getNumberOfSheets()
  println("#Sheets: ${numSheets}")
  0.upto(numSheets-1) { 
    sheet = wb.getSheetAt(it)
    println("Sheet ${it} ${sheet.sheetName}")
  }
}

class CryoLog 
{ 
  def name, id, hivStatus, dob, phone, cryoDate, actualReturn, enrolledSite, remarks
}

class AmpathLogbookUtils { 

  def processLogs() {
    // def filename = "/home/sgithens/code/ccsp-excel-import-dev/ccsp-logbook-raw-data-example.xlsx"
    def filename = "/home/sgithens/code/ccsp-excel-import-dev/ccsp-logbook-raw-data-example.xls"
    def wb = new HSSFWorkbook(new FileInputStream(filename))
    parseAMPATHLogBooks(wb)
  }

  def parseAMPATHLogBooks(wb) {
    def cryo = wb.getSheetAt(1)
    def cryologs = []
    cryo.eachWithIndex { it,idx ->
      if (idx == 0 || idx > 10) return

      def nextlog = new CryoLog()
      // 0 Name
      nextlog.name = it.getCell(0).stringCellValue
      nextlog.id = it.getCell(1).toString() 
      nextlog.cryoDate = it.getCell(5).dateCellValue

      def encutils = new EncounterUtils()
      def newEnc = encutils.makeEncounter()
      encutils.addObservation(newEnc, nextlog.cryoDate)
      println "Imported the stuff into the encoutners!"

      println nextlog.cryoDate
      it.eachWithIndex { cell,col ->
        print "${cell.cellType}  "
      }
      println ""
    } 
  }

}

class EncounterUtils { 

  def encService, obsService, personService, patientService
  def conceptService, locService

  EncounterUtils() { 
    encService = Context.getEncounterService()
    obsService = Context.getObsService()
    personService = Context.getPersonService()
    patientService = Context.getPatientService()
    conceptService = Context.getConceptService()
    locService = Context.getLocationService()
  }

  def makeEncounter(patientId=3,encTypeName="ADULTINITIAL",providerUserId=1) { 
    def provider = personService.getPerson(providerUserId)
    def enctype = encService.getEncounterType(encTypeName)
    def p = patientService.getPatient(patientId)
    def e = new Encounter()
    e.encounterDatetime = new Date()
    e.patient=p
    e.encounterType = enctype
    e.provider = provider
    encService.saveEncounter(e)
    return e
  }

  def addObservation(e,date) {
    def c = conceptService.getConcept(5497)
    def o = new Obs(e.patient,c,date,locService.getAllLocations()[1])
    obsService.saveObs(o,"Initial Creation")
    e.addObs(o)
    encService.saveEncounter(e)
  }

  // def testObs() { 
  //   def e = makeEncounter()
  //   addObservation(e)
  // }

}
