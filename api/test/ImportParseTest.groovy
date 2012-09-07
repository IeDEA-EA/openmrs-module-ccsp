/*
 * Tests for the IeDEA CCSP Code.
 */
import groovy.json.JsonSlurper
import au.com.bytecode.opencsv.CSVReader

import org.openmrs.module.ccsp.ConfigUtil
import org.openmrs.module.ccsp.CcspImport

class ImportParseTest extends GroovyTestCase { 
  def testFacesDataFile1 = "CCSPInitialForm_v7_results.csv"



  void testConfigFetch() {
    def config = ConfigUtil.getConfig("facesInitial")
    assertEquals "Check type", "csv", config.type
    assertEquals "Check a column", ["coded",7487,1115], config.columns.l603["1"]
  }

  void testRowConversion() {
    def config = ConfigUtil.getConfig("facesInitial")
    def reader = new CSVReader(new FileReader(testFacesDataFile1))
    def String [] header = reader.readNext()
    def rowToCheck = reader.readNext()
    def obj = CcspImport.parseRowIntoEncounter(config,header,rowToCheck)
    assertEquals "Make sure there are the right num of obs.", 1, obj.obs.size()
  }

  void testRowParsePreconditions() {
    shouldFail(IllegalArgumentException) {
      def obj = {}
      CcspImport.parseRowIntoEncounter(obj,["col1","col2"],[1,3,4])
    }
  }
  
  void testFacesVia() { 

    def util = new CcspImport(testFacesDataFile1, "facesInitial")
    util.parseAndImport()
    assertEquals "There should be 1 obs row.", util.size(), 1
  }

  void testUnsupportedSourceType() {
    shouldFail(UnsupportedOperationException) {
      def util = new CcspImport("CCSPInitialForm_v7_results.csv", "notASource")
    }
  }

  void testRowParser() {
    
  }

}