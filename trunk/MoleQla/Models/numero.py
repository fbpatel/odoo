from openerp.osv import fields, osv

class numero(osv.osv):
    
    _name = "numero"
    _description = "Numero"
    
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128, required=True),
        'numero' : fields.integer('Numero'),
        'articulos_id' : fields.one2many('articulo', 'numero_id','Articulos'),
        'fecha_p' : fields.date('Fecha de Publicacion'),
        'state':fields.selection([('start', 'Borrador'), ('builded', 'En construccion'),('a_publicar', 'Publicada'), ('voted', 'En votacion'),('closed', 'Cerrado')], 'Estado del numero'),
        }
    
    _defaults = {
                  'state':'start',
                  }
    
    
    
    def build(self, cr, uid, ids, context=None):
        self.write(cr, uid, ids, { 'state' : 'builded'})
        obj_articulo= self.pool.get('articulo')
        obj_seccion= self.pool.get('seccion')
        secciones = obj_seccion.search(cr, uid, [('id', '>', 0)])
        ides= []
        for seccion in secciones:
            seccion_ = obj_seccion.browse(cr, uid, seccion, context)
            ids_articulos= obj_articulo.search(cr, uid, [('state', '=', 'published'),('seccion_id', '=', seccion_.id)])
            if len(ids_articulos) > seccion_.max_articulos:
                i = 0
                for ide in ids_articulos:
                    i=i+1
                    if i<=seccion_.max_articulos:
                        ides.append(ide)
            else:
                for ide in ids_articulos:
                        ides.append(ide)             
        obj_articulo.write(cr, uid, ides, {'numero_id' : ids[0]})
     
    def send(self, cr, uid, ids, context=None):
        obj_articulo= self.pool.get('articulo')
        articulos = obj_articulo.search(cr, uid, [('numero_id', '=', ids[0])])
        obj_articulo.write(cr, uid, articulos, { 'state' : 'impress',  })
        obj_seccion = self.pool.get('seccion')
        secciones = obj_seccion.search(cr, uid, [('id', '>', 0)])
        ides= []
        for seccion in secciones:
            seccion_ = obj_seccion.browse(cr, uid, seccion, context)
            ids_articulos= obj_articulo.search(cr, uid, [('state', '=', 'impress'),('numero_id','=',ids[0]),('seccion_id', '=', seccion_.id)])
            if len(ids_articulos) > 0:
                obj_revisor = self.pool.get('editor')
                editor_id = obj_revisor.search(cr, uid, [('seccion_id', '=', seccion_.id)])
                editor = obj_revisor.browse(cr, uid, editor_id, context)
                revisor_id = editor[0].user_id.id
                obj_destaque = self.pool.get('destaque_articulos')
                vals = {'seccion_id':seccion_.id,'revisor_id':revisor_id,'numero_id':ids[0]}
                obj_destaque.create(cr, 1, vals, context=None)
                 
        
        self.write(cr, uid, ids, { 'state' : 'a_publicar',  })
        
        
         
    def vote(self, cr, uid, ids, context=None):
        self.write(cr, uid, ids, { 'state' : 'voted'})
         
    def close(self, cr, uid, ids, context=None):
        self.write(cr, uid, ids, { 'state' : 'closed'})
        
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            numero_name = record.nombre
            numero_num = record.numero
            
            string = numero_name + " (" + str(numero_num) + ")"
            
            
            res.append((record.id, string))
        return res
            
       
       
        
numero()