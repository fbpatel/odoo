from openerp.osv import fields, osv

class revision(osv.osv):
    
    _name = "revision"
    _description = "Revision"
    
    _columns = {       
        'articulo_id': fields.many2one('articulo','Articulo'), 
        'filename': fields.char('Filename'), 
        'seccion_id': fields.many2one('seccion','Seccion'),
        'observaciones': fields.binary('Observaciones'),
        'filenameObv': fields.char('FilenameObv'),  
        'revisor_id': fields.integer('Editor'),
        'state':fields.selection([('start', 'En Revision'), ('send', 'Aceptado'), ('cancel', 'Rechazado')], 'Estado de la revision'),
        'comentarios': fields.text('Comentarios'),
        'versiones_anteriores' : fields.one2many('articulo', 'old_revision_id','Version anterior'),
        'articulo_nombre' : fields.related('articulo_id', 'nombre', string='Nombre', type='text', readonly=True),
        'articulo_descripcion' : fields.related('articulo_id', 'descripcion', string='Descripcion', type='text', readonly=True),
        'articulo_seccion' : fields.related('articulo_id', 'seccion_id', string='Seccion', type='many2one', relation='seccion',readonly=True),      
        'articulo_tipoArticulo' : fields.related('articulo_id', 'tipo_articulo', string='Tipo Articulo', type='text', readonly=True),
        'articulo_tipoAutor' : fields.related('articulo_id', 'tipo_autor', string='Tipo Autor', type='text', readonly=True),
        'filenameArt': fields.char('FilenameObv'),
        'articulo_archivo' : fields.related('articulo_id', 'archivo', string='Archivo', type='binary', readonly=True)
        }
    
    _defaults = {
                  'state':'start',
                  'filenameObv':'observaciones.pdf',
                  }
    
    
    
    def aceptar(self, cr, uid, ids, context=None):
        revision = self.browse(cr, uid, ids, context)
        self.write(cr, uid, ids, { 'state' : 'send' })
        articulo_obj = self.pool.get('articulo')
        
        maquetador_obj = self.pool.get('maquetador')
        maquetador_id = maquetador_obj.search(cr, uid, [('seccion_id', '=', revision.seccion_id.id)])
        maquetador = maquetador_obj.browse(cr, uid, maquetador_id, context)
        vals = {'articulo_id':revision.articulo_id.id,'seccion_id':revision.seccion_id.id,'maquetador_id':maquetador[0].user_id.id}
        maquetacion_obj = self.pool.get('maquetacion')
        maquetacion_obj.create(cr, 1, vals,context=None)
        maquetacion_id = maquetacion_obj.search(cr, uid, [('articulo_id', '=', revision.articulo_id.id)])
        articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'editing', 'maquetacion_id':maquetacion_id[0] })
        
        # -------------------------------------------
        #Correo al maquetador de seccion
        #2. Mediante el articulo                
        # Obtenemos el maquetador
        user_maquetador_obj = self.pool.get('res.users')
        user_maquetador = user_maquetador_obj.browse(cr, 1, maquetador.user_id.id, context)
        email_maquetador= user_maquetador.login
        
        # Asunto y texto del email
        asunto = "Articulo Nuevo " + revision.articulo_id.nombre
        texto = "Se ha recibido un nuevo articulo."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email_maquetador, asunto, texto)
        # -------------------------------------------
        
        # -------------------------------------------
        # Correo al autor
        #2. Mediante el articulo
        autor_user_obj = self.pool.get('res.users')
        autor_user = autor_user_obj.browse(cr, uid, revision.articulo_id.user_id, context)
        email_autor = autor_user.login
        estado = "en maquetacion"
        
        # Asunto y texto del email
        asunto = "Articulo " + revision.articulo_id.nombre
        texto = "Su articulo ha cambiado su estado a <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email_autor, asunto, texto)
        # -------------------------------------------  
        
    def rechazar(self, cr, uid, ids, context=None):
        revision = self.browse(cr, uid, ids, context)
        self.write(cr, uid, ids, { 'state' : 'cancel' })
        articulo_obj = self.pool.get('articulo') 
        articulo_id = articulo_obj.search(cr, uid, [('revision_id', '=', revision.id)])
        articulo = articulo_obj.browse(cr, uid, articulo_id, context)
        
        vals = {'seccion_id':revision.seccion_id.id,
                'archivo':articulo.archivo,'filename':articulo.filename,'nombre':articulo.nombre,
                'tipo_articulo':articulo.tipo_articulo,'tipo_autor':articulo.tipo_autor,
                'palabras_clave':articulo.palabras_clave,'user_id':1,'old_revision_id':revision.id}
        articulo_obj.create(cr, 1, vals, context=None)
        articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'cancel' })
        
        #2. Mediante el articulo
        autor_user_obj = self.pool.get('res.users')
        autor_user = autor_user_obj.browse(cr, uid, revision.articulo_id.user_id, context)
        email_autor = autor_user.login
        estado = "rechazado por el editor de seccion"
        
        # Asunto y texto del email
        asunto = "Articulo " + revision.articulo_id.nombre
        texto = "Su articulo ha cambiado su estado a <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email_autor, asunto, texto)  
        
    
    
    
    
revision()