module.exports = function (app, swig, gestorBD, logger) {
    app.get('/amigos', function (req, res) {
        let criterio = {
            "_id": gestorBD.mongo.ObjectID(req.session.usuarioId),
        }
        gestorBD.obtenerUsuario(criterio, function (user) {
            let amigos = user.friend_ids
            let searchText = req.query.searchText;
            if(searchText === undefined)
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
                    }, {"email": {
                            $regex: ".*" + searchText + ".*",
                            $options: 'i'}
                    }],
                $and: [
                    {
                        "_id": {$in: amigos}
                    }
                ]
            };
            let pg = parseInt(req.query.pg);
            if (req.query.pg === undefined)
                pg = 1;
            gestorBD.obtenerUsuariosPg(allIdsCriterio, pg, function (usuarios, total) {
                if (usuarios === null) {
                    logger.error(req.session.usuario +": Se ha producido un error al listar los amigos");
                    res.redirect("/error");
                } else {
                    total = user.friend_ids.length;
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
                    logger.info(req.session.usuario + ": Se ha mostrado la lista de amigos");
                    res.send(swig.renderFile("views/bamigos.html", {
                        "usuarioSession": req.session.usuario,
                        "users": usuarios,
                        "paginas": paginas,
                        "actual": pg,
                    }));
                }
            });
        })
    });
};