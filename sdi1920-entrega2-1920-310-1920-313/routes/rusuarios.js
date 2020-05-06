module.exports = function(app, swig, gestorBD) {
    app.get("/usuarios", function(req, res) {
        res.send("ver usuarios");
    });

    app.get("/registrarse", function(req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
    });

    app.post('/usuario', function(req, res) {
        let seguro = ""
        let criterio = {email : req.body.email}
        if(req.body.password !== "")
            seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');

        let usuario = {
            nombre : req.body.nombre,
            apellidos : req.body.apellidos,
            email : req.body.email,
            password : seguro,
        }
        let repeated = false;
        let repeatedUser;
        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if(usuarios == null|| usuarios.length == 0) {
                repeated = true;
                let mensajeError = "";
                if(usuario.nombre === ""){
                    mensajeError += "El campo nombre no puede estar vacio.";
                }else if(usuario.apellidos === ""){
                    mensajeError += "El campo apellidos no puede estar vacio.";
                }else if(usuario.email === ""){
                    mensajeError += "El campo email no puede estar vacio.";
                }else if(usuario.password === ""){
                    mensajeError += "El campo contraseña no puede estar vacio.";
                }else if(req.body.password !== req.body.repeatpassword){
                    mensajeError += "Las contraseñas no coinciden.";
                }

                gestorBD.insertarUsuario(usuario, mensajeError,function(id, mensajeError) {
                    if(id == null){
                        res.redirect("/registrarse"+"?mensaje=" + mensajeError);
                    } else{
                        res.redirect("/identificarse"+"?mensaje=Nuevo usuario registrado");
                    }
                });
            } else{
                mensajeError = "Este email ya esta en uso"
                gestorBD.insertarUsuario(usuario, mensajeError,function(id, mensajeError) {
                    if(id == null){
                        res.redirect("/registrarse"+"?mensaje=" + mensajeError);
                    } else{
                        res.redirect("/identificarse"+"?mensaje=Nuevo usuario registrado");
                    }
                });
            }});

    });

    app.get("/identificarse", function(req, res) {
        let respuesta = swig.renderFile('views/bidentificacion.html', {});
        res.send(respuesta);
    });

    app.post("/identificarse", function(req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {email : req.body.email,password : seguro}
        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if(usuarios == null|| usuarios.length == 0) {
                req.session.usuario = null;
                res.redirect("/identificarse"+ "?mensaje=Email o password incorrecto"+ "&tipoMensaje=alert-danger ");
            } else{
                req.session.usuario = usuarios[0].email;
                res.redirect("/publicaciones");
            }});
    });

    app.get('/desconectarse',function(req,res){
        req.session.usuario =null;
        res.send("Usuario desconectado");
    });

};