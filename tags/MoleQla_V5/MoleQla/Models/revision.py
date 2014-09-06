# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv
from openerp.tools.translate import _

class revision(osv.osv):
    
    _name = "revision"
    _description = "Revision"
    
    _columns = {       
        'articulo_id': fields.many2one('articulo','Artículo'), 
        'filename': fields.char('Filename'), 
        'seccion_id': fields.many2one('seccion','Sección'),
        'observaciones': fields.binary('Observaciones'),
        'filenameObv': fields.char('FilenameObv'),  
        'revisor_id': fields.integer('Editor'),
        'state':fields.selection([('start', 'Inicio'), ('send', 'Aceptado'), ('cancel', 'Revisando'),('cancel_2', 'Rechazado'),('published', 'A Publicar')], 'Estado de la revisión'),
        'comentarios': fields.text('Comentarios'),
        'versiones_anteriores' : fields.one2many('articulo', 'old_revision_id','Versiones anteriores'),
        'articulo_nombre' : fields.related('articulo_id', 'nombre', string='Nombre', type='text', readonly=True),
        'articulo_estado' : fields.related('articulo_id', 'state', string='Estado del artículo', type='text', readonly=True),
        'articulo_a_publicar' : fields.related('articulo_id', 'a_publicar', string='Artículo a publicar', type='boolean', readonly=True),
        'articulo_descripcion' : fields.related('articulo_id', 'descripcion', string='Descripción', type='text', readonly=True),
        'articulo_seccion' : fields.related('articulo_id', 'seccion_id', string='Sección', type='many2one', relation='seccion',readonly=True),      
        'articulo_tipoArticulo' : fields.related('articulo_id', 'tipo_articulo', string='Tipo Artículo', type='text', readonly=True),
        'articulo_tipoAutor' : fields.related('articulo_id', 'tipo_autor', string='Tipo Autor', type='text', readonly=True),
        'filenameArt': fields.char('FilenameArt'),
        'articulo_archivo' : fields.related('articulo_id', 'archivo', string='Archivo', type='binary', readonly=True),
        'articulo_archivoDiff' : fields.related('articulo_id', 'archivo_diff', string='Archivo Diferencias', type='binary', readonly=True),
        'filenameDiff': fields.char('FilenameDiff')
        }
    
    _defaults = {
                  'state':'start',
                  'filenameObv':'observaciones.pdf',
                  'filenameArt' : 'articulo.pdf',
                  'filenameDiff' : 'diferencias.pdf'
                  }
    
    _order = 'state desc, id desc'
    
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, 1, ids, context=context):
            revision_name = record.articulo_nombre
            
            
            res.append((record.id, revision_name))
        return res
    
    def aceptar(self, cr, uid, ids, context=None):
        revision = self.browse(cr, 1, ids, context)
        self.write(cr, 1, ids, { 'state' : 'send' })
        articulo_obj = self.pool.get('articulo')
        
        maquetador_obj = self.pool.get('maquetador')
        maquetador_id = maquetador_obj.search(cr, 1, [('seccion_id', '=', revision.seccion_id.id)])
        maquetador = maquetador_obj.browse(cr, 1, maquetador_id, context)
        vals = {'articulo_id':revision.articulo_id.id,'seccion_id':revision.seccion_id.id,'maquetador_id':maquetador[0].user_id.id}
        maquetacion_obj = self.pool.get('maquetacion')
        maquetacion_obj.create(cr, 1, vals,context=None)
        maquetacion_id = maquetacion_obj.search(cr, 1, [('articulo_id', '=', revision.articulo_id.id)])
        articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'maquetando', 'maquetacion_id':maquetacion_id[0] })
        
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
        try:       
            correo_obj.mail(cr, 1, email_maquetador, asunto, texto)
        except:
            print "ERROR: No ha sido posible enviar el correo a"+email_maquetador
        # -------------------------------------------
        
        # -------------------------------------------
        # Correo al autor
        #2. Mediante el articulo
        autor_user_obj = self.pool.get('res.users')
        autor_user = autor_user_obj.browse(cr, 1, revision.articulo_id.user_id, context)
        email_autor = autor_user.login
        estado = "en maquetacion"
        
        # Asunto y texto del email
        asunto = "Articulo " + revision.articulo_id.nombre
        texto = "Su articulo ha cambiado su estado a <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo') 
        try:       
            correo_obj.mail(cr, 1, email_autor, asunto, texto)
        except:
            print "ERROR: No ha sido posible enviar el correo a"+email_autor
        # -------------------------------------------  
        
    def rechazar(self, cr, uid, ids, context=None):
        revision = self.browse(cr, 1, ids, context)
        if revision.observaciones == None:
            raise osv.except_osv(_('Warning!'), _("Es necesario añadir un archivo con las observaciones para rechazar el articulo."))
        else:
            self.write(cr, 1, ids, { 'state' : 'cancel' })
            articulo_obj = self.pool.get('articulo') 
            articulo_id = articulo_obj.search(cr, 1, [('revision_id', '=', revision.id)])
            articulo = articulo_obj.browse(cr, 1, articulo_id, context)
            
            vals = {'seccion_id':revision.seccion_id.id,
                    'archivo':articulo.archivo,'filename':articulo.filename,'nombre':articulo.nombre,
                    'tipo_articulo':articulo.tipo_articulo,'tipo_autor':articulo.tipo_autor,
                    'palabras_clave':articulo.palabras_clave,'user_id':1,'old_revision_id':revision.id,
                    'state':'version_rechazada'}
            articulo_obj.create(cr, 1, vals, context=None)
            articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'rechazado_en_revision' ,'archivo_diff':None})
            
            #2. Mediante el articulo
            autor_user_obj = self.pool.get('res.users')
            autor_user = autor_user_obj.browse(cr, 1, revision.articulo_id.user_id, context)
            email_autor = autor_user.login
            estado = "rechazado por el editor de seccion"
            
            # Asunto y texto del email
            asunto = "Articulo " + revision.articulo_id.nombre
            texto = "Su articulo ha cambiado su estado a <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
            
            # Se envia el correo
            correo_obj = self.pool.get('correo') 
            try:       
                correo_obj.mail(cr, 1, email_autor, asunto, texto) 
            except:
                print "ERROR: No ha sido posible enviar el correo a"+email_autor 
        
    
    def publicarArt(self, cr, uid, ids, context=None):
        revision = self.browse(cr, 1, ids, context)
        articulo_obj = self.pool.get('articulo')   
        seccion_obj = self.pool.get('seccion') 
        max = seccion_obj.browse(cr,1,revision.seccion_id.id).max_articulos
        articulos_a_publicar =  articulo_obj.search(cr, 1, [('seccion_id', '=', revision.seccion_id.id),('a_publicar','=', 'TRUE'),('state','!=', 'publicado')])
        if len(articulos_a_publicar)< max:    
            self.write(cr, 1, ids[0], { 'state' : 'published'})
            articulo_obj.write(cr, 1, revision.articulo_id.id, { 'a_publicar' : 'TRUE'})
        else:
            raise osv.except_osv(_('Warning!'), _("No se pueden publicar mas articulos de esta seccion en el proximo numero."))
        
    def rechazar_fin(self, cr, uid, ids, context=None):
        self.write(cr, 1, ids, { 'state' : 'cancel_2' })
        revision = self.browse(cr, 1, ids, context)
        articulo_obj = self.pool.get('articulo')  
              
        articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'rechazado_fin'})
        

    
    
revision()