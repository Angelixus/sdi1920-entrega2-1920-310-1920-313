module.exports = function (app, swig, gestorBD, logger) {

    app.get("/usuarios", function (req, res) {
        let searchText = req.query.searchText;
        if(searchText === undefined)
            searchText = "";

        let criterio = {
            $or: [
            {
                "nombre": {
                    $regex: ".*" + searchText + ".*",
                    $options: 'i'
                }
            }, {
                "apellidos": {
                    $regex: ".*" + searchText + ".*",
                    $options: 'i'
                }
            }, {"email": {
                    $regex: ".*" + searchText + ".*",
                    $options: 'i'}
            }],
            $and: [
                {
                    "email": {$ne: req.session.usuario}
                }
            ]
        };
        let pg = parseInt(req.query.pg);
        if(req.query.pg === undefined)
            pg = 1;

        gestorBD.obtenerUsuariosPg(criterio, pg, function (usuarios, total) {
            if (usuarios === null) {
                res.redirect("/error");
            } else {
                let ultimaPg = total / 5;
                if(total % 5 > 0) {
                    ultimaPg = ultimaPg + 1;
                }
                let paginas = [];
                for(let i = pg - 2; i <= pg + 2; i++) {
                    if(i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }
                var methodsFriend = {
                    "isAlreadyFriend": function() {
                        let res = false;
                        for(let elementIndex in this.friend_ids) {
                            if(this.friend_ids[elementIndex].toString() === req.session.usuarioId) {
                                res = true;
                                break;
                            }
                        }
                        return res;
                    },
                    "didReceiveRequestFromLogged": function() {
                        let res = false;
                        for(let elementIndex in this.friendRequest_ids) {
                            if(this.friendRequest_ids[elementIndex].toString() === req.session.usuarioId) {
                                res = true;
                                break;
                            }
                        }
                        return res;
                    }
                }

                for(let userDB in usuarios) {
                    for(let method in methodsFriend) {
                        usuarios[userDB][method] = methodsFriend[method];
                    }
                }
                gestorBD.obtenerUsuario({"_id": gestorBD.mongo.ObjectID(req.session.usuarioId)}, function(usuarioFromDB) {
                    if(usuarioFromDB !== null) {
                        var method = {
                            "didReceiveRequestFromOther": function (user) {
                                let res = false;
                                for(let elementIndex in this.friendRequest_ids) {
                                    if(this.friendRequest_ids[elementIndex].toString() === user._id.toString()) {
                                        res = true;
                                        break;
                                    }
                                }
                                return res;
                            }
                        }
                        usuarioFromDB["didReceiveRequestFromOther"] = method["didReceiveRequestFromOther"]

                        logger.info(req.session.usuario +": Se ha mostrado la lista de usuarios");
                        res.send(swig.renderFile("views/busuarios.html", {
                            "usuarioSession": req.session.usuario,
                            "users": usuarios,
                            "paginas": paginas,
                            "actual": pg,
                            "usuarioSessionObj": usuarioFromDB
                        }));
                    } else {
                        logger.error(req.session.usuario +": Se ha producido un error al listar los usuarios");
                        res.redirect("/error");
                    }
                });
            }
        })
    });

    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
    });

    app.post('/usuario', function (req, res) {
        let seguro = ""
        let criterio = {email: req.body.email}
        if (req.body.password !== "")
            seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');

        let usuario = {
            nombre: req.body.nombre,
            apellidos: req.body.apellidos,
            email: req.body.email,
            password: seguro,
            friend_ids: [],
            friendRequest_ids: []
        }
        let repeated = false;
        let repeatedUser;
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                repeated = true;
                let mensajeError = "";
                if (usuario.nombre === "") {
                    mensajeError += "El campo nombre no puede estar vacio.";
                } else if (usuario.apellidos === "") {
                    mensajeError += "El campo apellidos no puede estar vacio.";
                } else if (usuario.email === "") {
                    mensajeError += "El campo email no puede estar vacio.";
                } else if (usuario.password === "") {
                    mensajeError += "El campo contraseña no puede estar vacio.";
                } else if (req.body.password !== req.body.repeatpassword) {
                    mensajeError += "Las contraseñas no coinciden.";
                }

                gestorBD.insertarUsuario(usuario, mensajeError, function (id, mensajeError) {
                    if (id == null) {
                        logger.info("Ha habido un intento de registro fallido");
                        res.redirect("/registrarse" + "?mensaje=" + mensajeError + "&tipoMensaje=alert-danger");
                    } else {
                        logger.info("Nuevo usuario " + usuario.email + ", registrado");
                        res.redirect("/identificarse" + "?mensaje=Nuevo usuario registrado.");
                    }
                });
            } else {
                mensajeError = "Este email ya esta en uso."
                gestorBD.insertarUsuario(usuario, mensajeError, function (id, mensajeError) {
                    if (id == null) {
                        res.redirect("/registrarse" + "?mensaje=" + mensajeError + "&tipoMensaje=alert-danger");
                    } else {
                        res.redirect("/identificarse" + "?mensaje=Nuevo usuario registrado.");
                    }
                });
            }
        });

    });

    app.get("/identificarse", function (req, res) {
        let respuesta = swig.renderFile('views/bidentificacion.html', {});
        res.send(respuesta);
    });

    app.post("/identificarse", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {email: req.body.email}
        let mensajeError = "";
        if(criterio.email === "")
            mensajeError = "El campo email no puede estar vacio";

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                req.session.usuario = null;
                if(mensajeError === "")
                    mensajeError = "El email introducido no existe";

                logger.info("Ha habido un intento de conexion fallido");

                res.redirect("/identificarse" + "?mensaje=" + mensajeError + "&tipoMensaje=alert-danger ");
            }
            else if(seguro !== usuarios[0].password) {
                mensajeError = "La contraseña introducida no es correcta";
                if(req.body.password ==="")
                    mensajeError = "El campo contraseña no puede estar vacio"

                logger.info("Ha habido un intento de conexion fallido");

                res.redirect("/identificarse" + "?mensaje="+ mensajeError + "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                req.session.usuarioId = usuarios[0]._id;
                logger.info("El usuario " + req.session.usuario + " se ha conectado");
                res.redirect("/usuarios");
            }
        });
    });

    app.get('/desconectarse', function (req, res) {
        logger.level="info";
        logger.info("El usuario " + req.session.usuario + " se ha desconectado");

        req.session.usuario = null;
        req.session.usuarioId = null;

        res.redirect("/identificarse");
    });
};