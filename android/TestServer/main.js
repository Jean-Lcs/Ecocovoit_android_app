let express = require("express");
let bodyParser = require("body-parser");

var app = express();

//********** Middlewares

// text body parser
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.text({ type : "text/plain" }));

app.all("/", (req, res) => {

	console.log("Receive : "+req.body);
	res.send("ok");

	/*if(req.body == "Covoit") {
		console.log("\tRespond : ok");
		res.send("ok");
	}
	else {
		console.log("\tRespond : Error Bad bad request");
		res.sendStatus(400);
	}*/
});

// Start listening
app.listen(8080);