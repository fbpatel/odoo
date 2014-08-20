from openerp.osv import fields, osv

class numero(osv.osv):
    
    _name = "numero"
    _description = "Numero"
    
    def _getUlt(self, cr, uid, ids, context=None):
        cr.execute('select MAX(numero) from numero')
        id_returned = cr.fetchone()
        
        if not id_returned[0]:
            num_numero = 1
        else:
            num_numero = id_returned[0] + 1
        
        return num_numero
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128, required=True),
        'numero' : fields.integer('Numero', readonly=True),
        'articulos_id' : fields.one2many('articulo', 'numero_id','Articulos'),
        'fecha_p' : fields.date('Fecha de Publicacion'),
        'state':fields.selection([('start', 'Borrador'), ('builded', 'En construccion'),('a_publicar', 'Publicada'), ('voted', 'En votacion'),('closed', 'Cerrado')], 'Estado del numero'),
        }
    
    _defaults = {
                  'state':'start',
                  'numero': _getUlt
                  }
    
    def create(self, cr, uid, vals, context=None):
        numero_obj = self.pool.get('numero')
        numeros_start = numero_obj.search(cr, 1, [('state', '=', 'start')])
        numeros_builded = numero_obj.search(cr, 1, [('state', '=', 'builded')])
        
        if not numeros_start:      
            if not numeros_builded:    
                return super(numero, self).create(cr, 1, vals, context)            
              
    
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
        obj_editor = self.pool.get('editor')
        obj_maquetador = self.pool.get('maquetador')
        obj_votacion = self.pool.get('votacion')
        obj_articulo = self.pool.get('articulo')
        obj_linea_votacion = self.pool.get('linea_votacion')
        editores = obj_editor.search(cr, 1, [('id', '>', 0)])
        maquetadores = obj_maquetador.search(cr, 1, [('id', '>', 0)])
        articulos = obj_articulo.search(cr, 1, [('numero_id', '=', ids[0]),('destacado', '=', 'TRUE')])  
        for editor in editores:
            editor_ = obj_editor.browse(cr, 1, editor, context) 
            vals = {}
            vals = {'user_id':editor_.user_id.id,'numero_id':ids[0]}
            obj_votacion.create(cr, 1, vals, context=None)
            for articulo in articulos:
                vals_linea = {}
                votacion_id = obj_votacion.search(cr, 1, [('numero_id', '=', ids[0]),('user_id', '=', editor_.user_id.id)])
                vals_linea = {'votacion_id':votacion_id[0],'articulo':articulo}
                obj_linea_votacion.create(cr, 1, vals_linea, context=None) 
        for maquetador in maquetadores:
            maquetador_ = obj_maquetador.browse(cr, 1, maquetador, context) 
            vals = {}
            vals = {'user_id':maquetador_.user_id.id,'numero_id':ids[0]}
            obj_votacion.create(cr, 1, vals, context=None)
            for articulo in articulos:
                vals_linea = {}
                votacion_id = obj_votacion.search(cr, 1, [('numero_id', '=', ids[0]),('user_id', '=', maquetador_.user_id.id)])
                vals_linea = {'votacion_id':votacion_id[0],'articulo':articulo}
                obj_linea_votacion.create(cr, 1, vals_linea, context=None)       
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