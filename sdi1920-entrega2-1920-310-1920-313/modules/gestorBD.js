module.exports = {
    "mongo" : null,
    "app" : null,
    "init" : function(app, mongo) {
        this.mongo = mongo;
        this.app = app;
    },
    "insertarCancion" : function (cancion, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db){
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('canciones');
                collection.insert(cancion, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result.ops[0]._id);
                    db.close();
                });
            }
        });
    },
    "obtenerCanciones" : function(critero, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('canciones');
                collection.find(critero).toArray(function(err, canciones) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(canciones);
                    db.close();
                });
            }
        });
    },
    "insertarUsuario" : function (usuario, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('usuarios');
                collection.insert(usuario, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result.ops[0]._id);
                    db.close();
                });
            }
        });
    },
    "obtenerUsuarios" : function(criterio, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('usuarios');
                collection.find(criterio).toArray( function (err, usuarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(usuarios);
                    db.close();
                });
            }
        })
    },
    "modificarCancion" : function(criterio, cancion, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('canciones');
                collection.update(criterio, {$set : cancion}, function (err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result);
                    db.close();
                });
            }
        });
    },

    "obtenerComentarios" : function(criterio, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('comentarios');
                collection.find(criterio).toArray(function(err, comentarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(comentarios);
                    db.close();
                });
            }
        });
    },
    "insertarComentario" : function (comentario, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('comentarios');
                collection.insert(comentario, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result.ops[0]._id);
                    db.close();
                });
            }
        });
    },
    "eliminarCancion" : function(criterio, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('canciones');
                collection.remove(criterio, function(err, result) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(result);

                    db.close();
                });
            }
        });
    },
    "insertarCompra" : function(compra, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('compras');
                collection.insert(compra, function(err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },
    "obtenerCompras" : function(criterio, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('compras');
                collection.find(criterio).toArray(function(err, usuarios) {
                    if(err)
                        functionCallback(null);
                    else
                        functionCallback(usuarios);

                    db.close();
                });
            }
        });
    },
    "obtenerCancionesPg" : function(criterio,pg,funcionCallback){
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('canciones');
                collection.count(function(err, count){
                    collection.find(criterio).skip( (pg-1)*4 ).limit( 4 )
                        .toArray(function(err, canciones) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(canciones, count);
                            }
                            db.close();
                        });
                });
            }
        });
    },

    "encontrarCancionDeUsuario" : function(criterio, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('canciones');
                collection.find(criterio).toArray(function (err, canciones) {
                    if(err)
                        functionCallback(null);
                    else {
                        if(canciones.length === 1)
                            functionCallback(true);
                        else
                            functionCallback(false);
                        db.close();
                    }
                });
            }
        });
    },
    "encontrarCompraDeUsuario" : function(criterio, functionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if(err)
                functionCallback(null);
            else {
                let collection = db.collection('compras');
                collection.find(criterio).toArray(function (err, canciones) {
                    if(err)
                        functionCallback(null);
                    else {
                        if(canciones.length >= 1)
                            functionCallback(true);
                        else
                            functionCallback(false);
                        db.close();
                    }
                });
            }
        });
    }
};