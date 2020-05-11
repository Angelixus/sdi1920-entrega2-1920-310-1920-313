module.exports = {
    "mongo" : null,
    "app" : null,
    "init" : function(app, mongo) {
        this.mongo = mongo;
        this.app = app;
    },
    "insertarUsuario" : function (usuario, mensajeError, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err || mensajeError !== "")
                functionCallback(null, mensajeError);
            else {
                let collection = client.db(dbname).collection('usuarios');
                collection.insert(usuario, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result.ops[0]._id);
                    client.close();
                });
            }
        });
    },
    "obtenerUsuarios" : function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('usuarios');
                collection.find(criterio).toArray( function (err, usuarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(usuarios);
                    client.close();
                });
            }
        })
    },
    "updateUsuario": function (filter, updateOperation, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('usuarios');
                collection.updateMany(filter, updateOperation, function (err, result) {
                    if(err)
                        functionCallback(false);
                    else
                        functionCallback(true);
                })
            }
        });
    },
    "obtenerUsuario": function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('usuarios');
                collection.find(criterio).toArray( function (err, usuarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(usuarios[0]);
                    client.close();
                });
            }
        })
    },
    "obtenerUsuariosPg" : function(criterio,pg,funcionCallback){
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, client) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = client.db(dbname).collection('usuarios');
                collection.count(function(err, count){
                    collection.find(criterio).skip( (pg-1)*5 ).limit( 5 )
                        .toArray(function(err, usuarios) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(usuarios, count);
                            }
                            client.close();
                        });
                });
            }
        });
    },
    "obtenerAmigos" : function(criterio, funcionCallback){
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, client) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = client.db(dbname).collection('usuarios');
                collection.count(function(err, count){
                    collection.find(criterio).toArray(function(err, amigos) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(amigos, count);
                            }
                            client.close();
                        });
                });
            }
        });
    },
    "insertarMensaje" : function (mensaje, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('mensajes');
                collection.insert(mensaje, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result.ops[0]._id);
                    client.close();
                });
            }
        });
    },
    "obtenerMensajes" : function (criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('mensajes');
                collection.find(criterio).toArray(function (err, mensajes) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(mensajes)
                    client.close();
                });
            }
        });
    },
    "obtenerMensaje" : function (criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('mensajes');
                collection.find(criterio).toArray(function (err, mensajes) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(mensajes[0])
                    client.close();
                });
            }
        });
    },
    "updateMessage": function (filter, updateOperation, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('mensajes');
                collection.updateMany(filter, updateOperation, function (err, result) {
                    if(err)
                        functionCallback(false);
                    else
                        functionCallback(true);
                })
            }
        });
    },
};