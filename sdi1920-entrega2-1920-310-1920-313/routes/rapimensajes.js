module.exports = function (app, gestorBD) {

    app.get("/api/amigos", function (req, res) {
        let criterio = {
            email: req.session.usuario
        }
        gestorBD.obtenerUsuario(criterio, function (usuario) {
            if (usuario == null) {
                res.status(500);
                res.json({error: "se ha producido un error"})
            } else {
                let searchText = req.query.searchText;
                if (searchText === undefined)
                    searchText = "";
                let allIdsCriterio = {
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
                        }, {
                            "email": {
                                $regex: ".*" + searchText + ".*",
                                $options: 'i'
                            }
                        }],
                    $and: [
                        {
                            "_id": {$in: usuario.friend_ids}
                        }
                    ]
                };

                gestorBD.obtenerAmigos(allIdsCriterio, function (amigos) {
                    if (amigos == null) {
                        res.status(500);
                        res.json({error: "se ha producido un error"})
                    } else {
                        res.status(200);
                        res.send(JSON.stringify(amigos));
                    }
                })


            }
        });
    });

    app.post("/api/mensajes/sinleer", function (req, res) {
        let criterio = {
            "emisor": req.body.amigo,
            "destino": req.session.usuario,
            "leido": false
        }
        gestorBD.obtenerMensajes(criterio, function (mensajes) {
            if(mensajes === null) {
                res.status(500);
                res.json({error: "se ha producido un error"})
            } else {
                res.status(200)
                res.send({ "sinLeer": mensajes.length })
            }
        })
    })

    app.post("/api/mensaje",function(req,res){
        gestorBD.obtenerUsuario({email: req.body.destino}, function(dest){
            if(dest == null){
                res.status(500);
                res.json({error :"Error: Ha ocurrido un error"})
            } else {
                let nowTime = Date.now();
                var mensaje = {
                    emisor: req.session.usuario,
                    destino: dest.email,
                    texto: req.body.texto,
                    leido: false,
                    fecha_creacion: nowTime
                }
                gestorBD.obtenerUsuario({email: mensaje.emisor}, function(emis){
                    if(emis == null){
                        res.status(500);
                        res.json({error :"Error: Ha ocurrido un error"})
                    }else{
                        var criterioEmisor = {
                            friend_ids: emis.friend_ids
                        }
                        gestorBD.obtenerAmigos(criterioEmisor, function(amigos){
                            if(amigos==null){
                                res.status(500);
                                res.json({error :"Error: Ha ocurrido un error"})
                            } else {
                                let found=false;
                                for(i = 0; i<amigos.length; i++){
                                    let temp = amigos[i].email;
                                    if(emis.email === temp);
                                    found = true;
                                }
                                if(!found){
                                    res.status(500);
                                    res.json({error :"Error: No eres amigo de este usuario"})
                                }else{
                                    gestorBD.insertarMensaje(mensaje,function(id){
                                        if(id ==null){
                                            res.status(500);
                                            res.json({error :"Error: Ha ocurrido un error"})
                                        } else {
                                            res.status(201);
                                            res.json({mensaje :"mensaje insertado", _id :id})
                                        }
                                    });
                                }
                            }
                        });
                    }
                })
            }
        });
    });

    app.post('/api/mensaje/leido', function (req, res) {
        gestorBD.obtenerMensaje({"_id": gestorBD.mongo.ObjectID(req.body.messageId)}, function(mensaje) {
            if(mensaje === null) {
                res.status(500);
                res.json({error: "se ha producido un error"})
            } else {
                if(mensaje.destino === req.session.usuario) {
                    let newMessage = {
                        "_id": mensaje._id,
                        "emisor": mensaje.emisor,
                        "destino": mensaje.destino,
                        "texto": mensaje.texto,
                        "leido": true,
                        "fecha_creacion": mensaje.fecha_creacion
                    }
                    gestorBD.updateMessage({"_id": mensaje._id}, {$set: newMessage}, function(result) {
                        if(result === false) {
                            res.status(500);
                            res.json({error: "se ha producido un error"})
                        } else {
                            res.status(200);
                            res.json({mensaje: "se ha marcado el mensaje como leido"})
                        }
                    })
                } else {
                    res.status(403);
                    res.json({error: "El usuario no es el receptor del mensaje"});
                }
            }
        });
    })

    app.post("/api/autenticar/", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');

        let criterio = {
            email: req.body.email,
            password: seguro
        }

        gestorBD.obtenerUsuario(criterio, function (usuario) {
            if (usuario == null) {
                res.status(401); //Unathorized
                res.json({
                    autenticado: false
                })
            } else {
                var token = app.get('jwt').sign({
                    usuario: criterio.email,
                    tiempo: Date.now() / 1000
                }, "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token,
                    emisor: criterio.email
                })
            }
        });
    });

    app.post('/api/mensajes', function (req, res) {
        gestorBD.obtenerUsuario({"email": req.body.requestUser}, function (usuarioSession) {
            if (usuarioSession === null) {
                res.status(500);
                res.json({error: "se ha producido un error"});
            } else {
                gestorBD.obtenerUsuario({"email": req.body.requestOtherUser}, function (usuarioTarget) {
                    if (usuarioTarget === null) {
                        res.status(500);
                        res.json({error: "se ha producido un error"});
                    } else {
                        let areFriends = false;
                        for (let friendIdIndex in usuarioSession.friend_ids) {
                            if (usuarioSession.friend_ids[friendIdIndex].equals(usuarioTarget._id)) {
                                areFriends = true;
                                break;
                            }
                        }
                        if (areFriends) {
                            let criterio = {
                                $or: [
                                    {
                                        $and: [
                                            {"emisor": req.body.requestUser},
                                            {"destino": req.body.requestOtherUser}
                                        ]
                                    },
                                    {
                                        $and: [
                                            {"emisor": req.body.requestOtherUser},
                                            {"destino": req.body.requestUser}
                                        ]
                                    }
                                ],
                            }
                            gestorBD.obtenerMensajes(criterio, function (mensajes) {
                                if (mensajes === null) {
                                    res.status(500);
                                    res.json({error: "Se ha producido un error y no se pueden obtener los mensajes"});
                                } else {
                                    res.status(200);
                                    res.json({"mensajes": mensajes});
                                }
                            });
                        } else {
                            res.status(403);
                            res.json({error: "Los usuarios no son amigos"});
                        }
                    }
                });
            }
        });
    })
}