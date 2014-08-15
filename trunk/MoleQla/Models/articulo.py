from openerp.osv import fields, osv

class articulo(osv.osv):
    
    _name = "articulo"
    _description = "Articulo"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128, required=True),
        'tipo_articulo':fields.selection([('interno', 'Interno'), ('externo', 'Externo')], 'Tipo de Articulo', required=True),
        'tipo_autor':fields.selection([('divulgativo', 'Divulgativo'), ('investigacion', 'Investigacion')], 'Tipo de Autor', required=True),
        'archivo': fields.binary('Archivo', filters='*.pdf"', required=True),
        'descripcion': fields.text('Descripcion'),
        'palabras_clave': fields.text('Palabras Claves'),
        'filename': fields.char('Filename'),
        'user_id': fields.integer('Usuario'),
        'seccion_id': fields.many2one('seccion', 'Seccion'),
        'state':fields.selection([('start', 'Borrador'), ('send', 'Enviado'), ('cancel', 'Rechazado'),
                                  ('editing', 'En Maquetacion'), ('cancel_m', 'No Maquetado'), ('published', 'Publicable')], 'Estado del Articulo'),
        'revision_id': fields.many2one('revision', 'Revision'),
        'revision_observaciones' : fields.related('revision_id', 'observaciones', string="Observaciones", type="binary", readonly=True),
        'maquetacion_id': fields.many2one('maquetacion', 'Maquetacion'),
        'old_revision_id' : fields.many2one('revision', 'Revision'),
        'old_maquetacion_id' : fields.many2one('maquetacion', 'Maquetacion'),
        'numero_id' : fields.many2one('numero', 'Numero'),
        'fecha_maq' : fields.date('Fecha de Aceptacion')
        }
    
    _defaults = {
                  'state':'start',
                  }
    
    
    

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
        if ((articulo.state) == ('start')):
            
            editor_obj = self.pool.get('editor')
            revisor_id = editor_obj.search(cr, uid, [('seccion_id', '=', articulo.seccion_id.id)])
            revisor = editor_obj.browse(cr, uid, revisor_id, context)
            vals = {'articulo_id':ids[0], 'seccion_id':articulo.seccion_id.id, 'revisor_id':revisor[0].user_id.id}
            revision_obj.create(cr, 1, vals, context=None)
            revision_id = revision_obj.search(cr, uid, [('articulo_id', '=', articulo.id)])
            self.write(cr, uid, ids, { 'state' : 'send' , 'revision_id': revision_id[0]})
            estado = "enviado"
            
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
        
        #2. Mediante el articulo        
        autor_user_obj = self.pool.get('res.users')
        autor_user = autor_user_obj.browse(cr, uid, articulo.user_id, context)
        email_autor = autor_user.login
        
        # Asunto y texto del email
        asunto = "Articulo " + articulo.nombre
        texto = "Su articulo ha sido recibido por el equipo de MoleQla y se encuentra en el estado <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email_autor, asunto, texto)    
        
        
    
        
        
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            articulo_name = record.nombre
            
            
            res.append((record.id, articulo_name))
        return res
        
    
        
        
articulo()
