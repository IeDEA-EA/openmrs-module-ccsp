package org.openmrs.module.ccsp

import groovy.json.JsonSlurper
import au.com.bytecode.opencsv.CSVReader

class ConfigUtil {
  def static getConfig(sourceName) {
    def jsonConfig = new File("./ccsp-mappings.json").text
    def slurper = new JsonSlurper()
    def config = slurper.parseText(jsonConfig)
    def inconfig = config.inputs[sourceName]
    return inconfig
  }
}

class OpenMRSUtil {
  OpenMRSUtil() {

  }

  /*
   * Take the object format returned from CcspImport.parseRowIntoEncounter
   * and use it to enter an OpenMRS Encounter.
   */
  def createEncounter(obj) {

  }
}

class CcspImport { 
 
  def sources = ['facesInitial']
  def inconfig, dataFileName
  def size = 0

  /*
   * Open a new file for importing.
   * fileName: Physical location of file on disk
   * importType: Type of import (such as facesInitial, ampathViaLogbook)
   */
  CcspImport(fileName,importType) {
    if (!(importType in sources)) {
      throw new UnsupportedOperationException("Unsupported Input Type")
    }

    inconfig = ConfigUtil.getConfig(importType)
    dataFileName = fileName
  }

  /*
   * This function should have no side effects. Given an input config
   * object, sheet header, and a sheet row, it returns an object 
   * specifying the observations that will be created for the row
   * encounter. The object will look like:
   * 
   * object : {
   *   obs: [
   *     ["coded",7487,1115],
   *     ["coded",7487,7507]
   *   ]
   * }
   */
  def static parseRowIntoEncounter(config,header,row) {
    def togo = ["obs":[]]
    
    if (header.size() != row.size())
      throw new IllegalArgumentException(
        "The header and row lengths need to match to parse")
    
    for (int i = 0; i < row.length; i++) {
      def colname = header[i]
      if (config.columns[colname]) {
        def colconfig = config.columns[colname]
        togo.obs.push(colconfig[row[i]])
      }
    }

    return togo
  }

  /*
   * Run through the data source for this instance, and import it in to
   * OpenMRS.
   */
  def parseAndImport() {
    if (inconfig.type == "csv") {
      def reader = new CSVReader(new FileReader(dataFileName))
      def String [] header = reader.readNext()
      def String [] nextLine
      while ((nextLine = reader.readNext()) != null) {
        parseRowIntoEncounter(inconfig,header,nextLine)
        size++
      }
    }
  }

  /* 
   * Return the number of records that were read from the source.
   */
  def int size() {
    return size
  }
  
}