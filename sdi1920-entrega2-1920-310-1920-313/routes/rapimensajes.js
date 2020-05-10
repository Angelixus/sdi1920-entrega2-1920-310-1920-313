module.exports = function(app,gestorBD){

    app.get("/api/amigos",function(req,res){
        let criterio = {
            email : req.session.usuario
        }
        gestorBD.obtenerUsuario(criterio,function(usuario){
            if(usuario==null){
                res.status(500);
                res.json({error :"se ha producido un error"})
            } else{
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
                            "_id": {$in: usuario.friend_ids}
                        }
                    ]
                };

                gestorBD.obtenerAmigos(allIdsCriterio, function(amigos){
                    if(amigos == null){
                        res.status(500);
                        res.json({error :"se ha producido un error"})
                    }else{
                        res.status(200);
                        res.send(JSON.stringify(amigos));
                    }
                })


            }});
    });

    app.post("/api/mensaje",function(req,res){
        var mensaje = {
            emisor :req.session.usuario,
            destino :req.body.destino.email,
            texto :req.body.texto,
            leido :false
        }
        var criterio = {
            email: req.session.usuario.friend_ids
        }

        gestorBD.obtenerAmigos(criterio, function(amigos){
            if(amigos==null){
                res.status(500);
                res.json({error :"Error: Ha ocurrido un error"})
            } else {
                if(!amigos.contains(req.body.destino._id)){
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
    });


    app.post("/api/autenticar/", function(req, res) {
       let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
           .update(req.body.password).digest('hex');

       let criterio = {
           email : req.body.email,
           password : seguro
       }

       gestorBD.obtenerUsuario(criterio, function(usuario) {
           if(usuario == null){
               res.status(401); //Unathorized
               res.json({
                   autenticado : false
               })
           } else {
               var token = app.get('jwt').sign({
                   usuario: criterio.email,
                   tiempo: Date.now()/1000
               },"secreto");
               res.status(200);
               res.json({
                   autenticado : true,
                   token :token
               })
           }
       });
    });
}