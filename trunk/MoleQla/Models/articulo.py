# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class articulo(osv.osv):
    
    _name = "articulo"
    _description = "Articulo"
    
    def _get_autor(self, cr, uid, context=None):
        autor_obj = self.pool.get('autor')
        autor_id = autor_obj.search(cr, uid, [('user_id', '=', uid)])
        autor = autor_obj.browse(cr, uid, autor_id, context)
        res = autor.nombre
        return res


    def _set_autor(self, cr, uid, ids, name, args, context=None):
        res = {}
        print "\n\nset function call"  
        for i in self.browse(cr, uid, ids, context=context):
            autor_obj = self.pool.get('autor')
            autor_id = autor_obj.search(cr, uid, [('user_id', '=', i.user_id)])
            autor = autor_obj.browse(cr, uid, autor_id, context)
            res[i.id] = autor.nombre
        return res
        
        
        
        
        
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128, required=True),
        'tipo_autor':fields.selection([('interno', 'Interno'), ('externo', 'Externo')], 'Tipo de Autor', required=True),
        'tipo_articulo':fields.selection([('divulgativo', 'Divulgativo'), ('investigacion', 'Investigación')], 'Tipo de Artículo', required=True),
        'archivo': fields.binary('Archivo', filters='*.pdf"', required=True),
        'descripcion': fields.text('Resumen'),
        'palabras_clave': fields.text('Palabras Claves'),
        'filename': fields.char('Filename'),
        'user_id': fields.integer('Usuario'),
        'seccion_id': fields.many2one('seccion', 'Sección'),
        'state':fields.selection([('start', 'Borrador'), ('send', 'Enviado'), ('cancel', 'Rechazado'),
                                  ('editing', 'En Maquetación'), ('cancel_m', 'No Maquetado'), ('published', 'Publicable'), ('impress', 'Publicado')], 'Estado del Artículo'),
        'revision_id': fields.many2one('revision', 'Revision'),
        'revision_observaciones' : fields.related('revision_id', 'observaciones', string='Observaciones', type='binary', readonly=True),
        'revision_comentarios' : fields.related('revision_id', 'comentarios', string='Comentarios', type='text', readonly=True),
        'maquetacion_id': fields.many2one('maquetacion', 'Maquetación'),
        'maquetacion_observaciones' : fields.related('maquetacion_id', 'observaciones', string='Observaciones', type='binary', readonly=True),
        'maquetacion_comentarios' : fields.related('maquetacion_id', 'comentarios', string='Comentarios', type='text', readonly=True),
        'old_revision_id' : fields.many2one('revision', 'Revision'),
        'old_maquetacion_id' : fields.many2one('maquetacion', 'Maquetacion'),
        'numero_id' : fields.many2one('numero', 'Número'),
        'fecha_maq' : fields.date('Fecha de Aceptación'),
        'destacado' : fields.boolean('Destacado'),
        'premiado' : fields.boolean('Premiado'),
        'autor': fields.function(_set_autor,readonly=True, string='Autor',type='char'),
        'asignatura' : fields.char('Asignatura'),
        'tipo_autor_interno':fields.selection([('libre', 'Libre'), ('asignatura', 'Asignatura')],'Tipo de Autor Interno'),
        'mostrar_tipo_autor_interno' : fields.boolean("Muestra tipo autor interno"),
        'mostrar_asignatura' : fields.boolean("Muestra asignatura"),
        'archivo_diff' : fields.binary('Archivo Diferencias', filters='*.pdf"', help="Este archivo contendrá las diferencias entre la versión antigua y la versión nuevo del artículo"),        
        'filenameDiff': fields.char('FilenameDiff'),
        'filenameObv': fields.char('FilenameObv'),
        }
    
    _defaults = {
                  'state':'start',
                  'autor':_get_autor,
                  'mostrar_tipo_autor_interno':False,
                  'mostrar_asignatura': False,
                  'filenameDiff':'diferencias.pdf',
                  'filenameObv':'observaciones.pdf'
                  }
    
    _order = 'id desc'
    
    def onchange_tipo_autor(self, cr, uid, ids,tipo_autor, context=None):
        val = {}
        if tipo_autor == 'interno':
            val = {
                   'mostrar_tipo_autor_interno':True
                   }
        else:
            val = {
                   'mostrar_tipo_autor_interno':False
                   }
        return {'value' : val}
    
    def onchange_tipo_autor_interno(self, cr, uid, ids,tipo_autor_interno, context=None):
        val = {}
        if tipo_autor_interno == 'asignatura':
            val = {
                   'mostrar_asignatura':True
                   }
        else:
            val = {
                   'mostrar_asignatura':False
                   }
        return {'value' : val}

    def create(self, cr, uid, vals, context=None):
        if 'user_id' in vals.keys():  
            vals['user_id'] = 1
        else:
            vals['user_id'] = uid
            nombre = vals['nombre'] + '.pdf'      
            vals['filename'] = nombre
                       
        return super(articulo, self).create(cr, uid, vals, context)      
        
    
    def enviar(self, cr, uid, ids, context=None):
        estado = ""
        articulo = self.browse(cr, uid, ids, context)
        revision_obj = self.pool.get('revision')
        maquetacion_obj = self.pool.get('maquetacion')
        
        
        #DAO res.users
        user_obj = self.pool.get('res.users')
        if ((articulo.state) == ('start')):
            
            editor_obj = self.pool.get('editor')
            revisor_id = editor_obj.search(cr, uid, [('seccion_id', '=', articulo.seccion_id.id)])
            revisor = editor_obj.browse(cr, uid, revisor_id, context)
            vals = {'articulo_id':ids[0], 'seccion_id':articulo.seccion_id.id, 'revisor_id':revisor[0].user_id.id}
            revision_obj.create(cr, 1, vals, context=None)
            revision_id = revision_obj.search(cr, uid, [('articulo_id', '=', articulo.id)])
            self.write(cr, uid, ids, { 'state' : 'send' , 'revision_id': revision_id[0]})
            estado = "enviado"
            
            # -------------------------------------------
            # Correo al editor de seccion
            #2. Mediante el articulo    
            # Obtenemos el editor de seccion  
            user_editor = user_obj.browse(cr, 1, revisor.user_id.id, context)
            email_editor = user_editor.login
            
            # Asunto y texto del email
            asunto = "Articulo Nuevo " + articulo.nombre
            texto = "Se ha recibido un nuevo articulo."
            
            # Se envia el correo
            correo_obj = self.pool.get('correo') 
            
            try:       
                correo_obj.mail(cr, 1, email_editor, asunto, texto)
            except:
                print "ERROR: No ha sido posible enviar el correo a"+email_editor
            # -------------------------------------------
            
        if ((articulo.state) == ('cancel')):
            revision_id = revision_obj.search(cr, uid, [('articulo_id', '=', articulo.id)])
            self.write(cr, uid, ids, { 'state' : 'send' })
            revision_obj.write(cr, 1, revision_id, { 'state' : 'start' })
            estado = "enviado"
            
        if ((articulo.state) == ('cancel_m')):
            maquetacion_id = maquetacion_obj.search(cr, uid, [('articulo_id', '=', articulo.id)])
            self.write(cr, uid, ids, { 'state' : 'editing' })
            maquetacion_obj.write(cr, 1, maquetacion_id, { 'state' : 'start' })
            estado = "en maquetacion"
            
        # Obtenemos el correo del autor
        #=======================================================================
        # #1. Mediante el usurio logado
        # current_user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        # email = current_user.login
        #=======================================================================
        
        # -------------------------------------------
        # Correo al autor
        #2. Mediante el articulo        
        autor_user = user_obj.browse(cr, 1, articulo.user_id, context)
        email_autor = autor_user.login
        
        # Asunto y texto del email
        asunto = "Articulo " + articulo.nombre
        texto = "Su articulo ha sido recibido por el equipo de MoleQla y se encuentra en el estado <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')
        try:        
            correo_obj.mail(cr, 1, email_autor, asunto, texto)  
        except:
            print "ERROR: No ha sido posible enviar el correo a"+email_autor
        # -------------------------------------------
       
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            articulo_name = record.nombre
            
            
            res.append((record.id, articulo_name))
        return res
        
    
        
        
articulo()
