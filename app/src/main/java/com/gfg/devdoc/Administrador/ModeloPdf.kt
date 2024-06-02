package com.gfg.devdoc.Administrador

class ModeloPdf {
    var uid :String=""
    var id :String=""
    var titulo :String=""
    var descripcion :String=""
    var categoria :String=""
    var url :String=""
    var tiempo :Long=0
    var contadorVisitas :Long=0
    var contadorDescargas:Long=0
    var esfavorito=false

    constructor()
    constructor(
        uid: String,
        id: String,
        titulo: String,
        descripcion: String,
        categoria: String,
        url: String,
        tiempo: Long,
        contadorVisitas: Long,
        esfavorito:Boolean
    ) {
        this.uid = uid
        this.id = id
        this.titulo = titulo
        this.descripcion = descripcion
        this.categoria = categoria
        this.url = url
        this.tiempo = tiempo
        this.contadorVisitas = contadorVisitas
        this.contadorDescargas=contadorDescargas
        this.esfavorito=esfavorito
    }
}