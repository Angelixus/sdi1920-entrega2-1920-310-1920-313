let express = require('express');
let app = express();

let rest = require('request');
app.set('rest',rest);

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
    // Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});

var jwt = require('jsonwebtoken');
app.set('jwt',jwt);

let fs = require('fs');
let https = require('https');

let expressSession = require('express-session');
app.use(expressSession({
    "secret" : 'solo_leveling',
    "resave": true,
    "saveUninitialized" : true
}));
let crypto = require('crypto');

let fileUpload = require('express-fileupload');
app.use(fileUpload());
let mongo = require('mongodb');
let swig = require('swig');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

let gestorBD = require('./modules/gestorBD.js');
gestorBD.init(app, mongo);

var routerUsuarioToken = express.Router();
routerUsuarioToken.use(function(req, res, next) {
    // obtener el token, vía headers (opcionalmente GET y/o POST).
    var token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function(err, infoToken) {
            if (err || (Date.now()/1000 - infoToken.tiempo) > 240 ){
                res.status(403); // Forbidden
                res.json({
                    acceso : false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
                return;

            } else {
                // dejamos correr la petición
                req.session.usuario = infoToken.usuario;
                res.usuario = infoToken.usuario;
                next();
            }
        });

    } else {
        res.status(403); // Forbidden
        res.json({
            acceso : false,
            mensaje: 'No hay Token'
        });
    }
});
// Aplicar routerUsuarioToken
app.use('/api/cancion', routerUsuarioToken);

let routerUsuarioSession = express.Router();
routerUsuarioSession.use(function(req, res, next) {
    console.log("routerUsuarioSession");
    if(req.session.usuario)
        next();
    else {
        console.log("va a: " + req.originalUrl);
        res.redirect('/identificarse');
    }
});
let routerUsuarioAutor = express.Router();
routerUsuarioAutor.use(function(req, res, next) {
    console.log("routerUsuarioAutor");
    let path = require('path');
    let id = path.basename(req.originalUrl);
    gestorBD.obtenerCanciones(
        {_id: mongo.ObjectID(id) }, function (canciones) {
            console.log(canciones[0]);
            if(canciones[0].autor == req.session.usuario ){
                next();
            } else {
                res.redirect("/tienda");
            }
        })
});

app.use("/cancion/modificar",routerUsuarioAutor);
app.use("/cancion/eliminar",routerUsuarioAutor);
app.use('/canciones/agregar', routerUsuarioSession);
app.use('/publicaciones', routerUsuarioSession);
app.use("/cancion/comprar",routerUsuarioSession);
app.use("/compras",routerUsuarioSession);

let routerAudios = express.Router();
routerAudios.use(function(req, res, next) {
    console.log("routerAudios");
    let path = require('path');
    let idCancion = path.basename(req.originalUrl, '.mp3');

    gestorBD.obtenerCanciones({"_id" : mongo.ObjectID(idCancion)}, function(canciones) {
        if(req.session.usuario && canciones[0].autor == req.session.usuario)
            next();
        else {
            let criterio = {
                usuario : req.session.usuario,
                cancionId : mongo.ObjectID(idCancion)
            };

            gestorBD.obtenerCompras(criterio ,function(compras){
                if (compras != null && compras.length > 0 ){
                    next();
                    return;
                } else {
                    res.redirect("/tienda");
                }
            });
        }
    });
});

app.use('/audios/', routerAudios);

app.use(express.static('public'));

app.set('port', 8081);
app.set('db', 'mongodb://admin:solo_leveling@socialnetwork-shard-00-00-iaytk.mongodb.net:27017,socialnetwork-shard-00-01-iaytk.mongodb.net:27017,socialnetwork-shard-00-02-iaytk.mongodb.net:27017/test?ssl=true&replicaSet=socialnetwork-shard-0&authSource=admin&retryWrites=true&w=majority');
app.set('clave', 'solo_leveling');
app.set('crypto', crypto);

require('./routes/rusuarios.js')(app, swig, gestorBD);

app.get('/', function(req, res) {
    res.redirect('/tienda');
});

app.use(function(err, req, res, next) {
    console.log("Error producido: " + err);
    if(! res.headersSent) {
        res.status(400);
        res.send("Recurso no disponible");
    }
});

app.get('/error', function(req, res) {
    let respuesta = swig.renderFile('views/berror.html');
    res.send(respuesta)
});

https.createServer({
    key: fs.readFileSync('certificates/alice.key'),
    cert: fs.readFileSync('certificates/alice.crt')
}, app).listen(app.get('port'), function() {
    console.log("Servidor activo");
});