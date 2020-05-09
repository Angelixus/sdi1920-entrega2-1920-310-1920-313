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
    "modificarUsuario" : function(criterio, cancion, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('usuarios');
                collection.update(criterio, {$set : cancion}, function (err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result);
                    client.close();
                });
            }
        });
    },

    "obtenerComentarios" : function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('comentarios');
                collection.find(criterio).toArray(function(err, comentarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(comentarios);
                    client.close();
                });
            }
        });
    },
    "insertarComentario" : function (comentario, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('comentarios');
                collection.insert(comentario, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result.ops[0]._id);
                    client.close();
                });
            }
        });
    },
    "eliminarCancion" : function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('canciones');
                collection.remove(criterio, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result);

                    client.close();
                });
            }
        });
    },
    "insertarCompra" : function(compra, funcionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, client) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = client.db(dbname).collection('compras');
                collection.insert(compra, function(err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    client.close();
                });
            }
        });
    },
    "obtenerCompras" : function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('compras');
                collection.find(criterio).toArray(function(err, usuarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(usuarios);

                    client.close();
                });
            }
        });
    },
    "obtenerCancionesPg" : function(criterio,pg,funcionCallback){
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, client) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = client.db(dbname).collection('canciones');
                collection.count(function(err, count){
                    collection.find(criterio).skip( (pg-1)*4 ).limit( 4 )
                        .toArray(function(err, canciones) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(canciones, count);
                            }
                            client.close();
                        });
                });
            }
        });
    },

    "encontrarCancionDeUsuario" : function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('canciones');
                collection.find(criterio).toArray(function (err, canciones) {
                    if(err)
                        functionCallback(null);
                    else {
                        if(canciones.length === 1)
                            functionCallback(true);
                        else
                            functionCallback(false);
                        client.close();
                    }
                });
            }
        });
    },
    "encontrarCompraDeUsuario" : function(criterio, functionCallback) {
        let dbname = this.app.get('dbname');
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, client) {
            if(err)
                functionCallback(null);
            else {
                let collection = client.db(dbname).collection('compras');
                collection.find(criterio).toArray(function (err, canciones) {
                    if(err)
                        functionCallback(null);
                    else {
                        if(canciones.length >= 1)
                            functionCallback(true);
                        else
                            functionCallback(false);
                        client.close();
                    }
                });
            }
        });
    },
};