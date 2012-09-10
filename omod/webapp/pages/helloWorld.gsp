Hello, world. Welcome to <b>CCSP</b>.

${ ui.includeFragment("helloUser") }
${ ui.includeFragment("encountersTodayTwo", [
        start: "2011-02-16", 
        end: "2012-08-04",
        properties: ["location", "encounterDatetime"],
        decorator: "widget",
        decoratorConfig: [title: "Today's Encounters"]
]) }
