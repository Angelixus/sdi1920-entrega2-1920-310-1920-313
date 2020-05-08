module.exports = function (app, swig, gestorBD) {
    app.get("/user/:id/send", function (req, res) {
        gestorBD.obtenerUsuario({"_id": gestorBD.mongo.ObjectID(req.params.id)}, function (usuarioParametro) {
            gestorBD.obtenerUsuario({"_id": gestorBD.mongo.ObjectID(req.session.usuarioId)}, function (usuarioSession) {
                if (usuarioParametro === null) {
                    res.redirect("/error");
                } else {
                    if (req.session.usuarioId === req.params.id) {
                        res.redirect("/usuarios" + "?mensaje=No puedes enviar una solicitud de amistad a ti mismo");
                        return;
                    }
                    let areAlreadyFriends = false;
                    let sentFriendRequest = false;
                    for (let friendRequestIndex in usuarioParametro.friendRequest_ids) {
                        if (usuarioParametro.friendRequest_ids[friendRequestIndex].equals(gestorBD.mongo.ObjectID(req.session.usuarioId))) {
                            sentFriendRequest = true;
                            break;
                        }
                    }
                    for (let friendIndex in usuarioParametro.friend_ids) {
                        if (usuarioParametro.friend_ids[friendIndex].equals(gestorBD.mongo.ObjectID(req.session.usuarioId))) {
                            areAlreadyFriends = true;
                            break;
                        }
                    }

                    for (let friendOtherIndex in usuarioSession.friendRequest_ids) {
                        if (usuarioSession.friendRequest_ids[friendOtherIndex].equals(gestorBD.mongo.ObjectID(req.params.id))) {
                            sentFriendRequest = true;
                            break;
                        }
                    }

                    if (!areAlreadyFriends && !sentFriendRequest) {
                        let filter = {"_id": gestorBD.mongo.ObjectID(req.params.id)}
                        let updateOperation = {$push: {friendRequest_ids: gestorBD.mongo.ObjectID(req.session.usuarioId)}}
                        gestorBD.updateUsuario(filter, updateOperation, function (success) {
                            if (!success)
                                res.redirect("/usuarios" + "?mensaje=No se pudo enviar la peticion de amistad");
                            else
                                res.redirect("/usuarios");
                        })
                    } else {
                        if (sentFriendRequest) {
                            res.redirect("/usuarios" + "?mensaje=Ya se ha enviado o recibido una petición de amistad a este usuario");
                        } else if (areAlreadyFriends) {
                            res.redirect("/usuarios" + "?mensaje=Ya es amigo de este usuario");
                        }
                    }
                }
            })
        })
    });

    app.get('/peticiones', function (req, res) {
        let criterio = {
            "_id": gestorBD.mongo.ObjectID(req.session.usuarioId),
        }
        gestorBD.obtenerUsuario(criterio, function (user) {
            let peticiones = user.friendRequest_ids
            let allIdsCriterio = {
                "_id": {$in: peticiones}
            }
            let pg = parseInt(req.query.pg);
            if (req.query.pg === undefined)
                pg = 1;

            gestorBD.obtenerUsuariosPg(allIdsCriterio, pg, function (usuarios, total) {
                if (usuarios === null) {
                    res.redirect("/error");
                } else {
                    total = usuarios.length
                    let ultimaPg = total / 5;
                    if (total % 5 > 0) {
                        ultimaPg = ultimaPg + 1;
                    }
                    let paginas = [];
                    for (let i = pg - 2; i <= pg + 2; i++) {
                        if (i > 0 && i <= ultimaPg) {
                            paginas.push(i);
                        }
                    }
                    res.send(swig.renderFile("views/blistapeticionesdeamistad.html", {
                        "usuarioSession": req.session.usuario,
                        "users": usuarios,
                        "paginas": paginas,
                        "actual": pg,
                    }));
                }
            });
        })
    });
    app.get('/friendRequest/:id/accept', function (req, res) {
        gestorBD.obtenerUsuario({"_id": gestorBD.mongo.ObjectID(req.session.usuarioId)}, function (usuario) {
            if (usuario === null)
                res.redirect("/error");
            else {
                let hasFriendRequestId = false;
                let indexOfFriend = 0;
                for (let userIdIndex in usuario.friendRequest_ids) {
                    if (usuario.friendRequest_ids[userIdIndex].toString() === req.params.id) {
                        hasFriendRequestId = true;
                        indexOfFriend = userIdIndex;
                        break;
                    }
                }

                if (hasFriendRequestId) {
                    usuario.friendRequest_ids.splice(indexOfFriend, 1);
                    usuario.friend_ids.push(gestorBD.mongo.ObjectID(req.params.id));
                    let usuarioSession = {
                        "_id": usuario._id,
                        "nombre": usuario.nombre,
                        "apellidos": usuario.apellidos,
                        "email": usuario.email,
                        "password": usuario.password,
                        "friend_ids": usuario.friend_ids,
                        "friendRequest_ids": usuario.friendRequest_ids
                    }
                    gestorBD.modificarUsuario({"_id": usuario._id}, usuarioSession, function (result) {
                        gestorBD.obtenerUsuario({"_id": gestorBD.mongo.ObjectID(req.params.id)}, function (usuarioParam) {
                            usuarioParam.friend_ids.push(gestorBD.mongo.ObjectID(req.session.usuarioId));
                            let usuarioParamNew = {
                                "_id": usuarioParam._id,
                                "nombre": usuarioParam.nombre,
                                "apellidos": usuarioParam.apellidos,
                                "email": usuarioParam.email,
                                "password": usuarioParam.password,
                                "friend_ids": usuarioParam.friend_ids,
                                "friendRequest_ids": usuarioParam.friendRequest_ids
                            }
                            gestorBD.modificarUsuario({"_id": usuarioParam._id}, usuarioParamNew, function (result) {
                                res.redirect("/peticiones")
                            });
                        });
                    });
                } else {
                    res.redirect("/peticiones" + "?mensaje=No se puede aceptar una peticion de amistad que no existe    ");
                }
            }
        });
    });
};