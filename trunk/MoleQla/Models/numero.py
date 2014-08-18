from openerp.osv import fields, osv

class numero(osv.osv):
    
    _name = "numero"
    _description = "Numero"
    
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128, required=True),
        'numero' : fields.integer('Numero',),
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
                        #articulos[cont]=obj_articulo.browse(cr, uid, id, context)[0]
                        ides.append(ide)
            else:
                for ide in ids_articulos:
                        #articulos[cont]=obj_articulo.browse(cr, uid, id, context)[0]
                        ides.append(ide)             
        obj_articulo.write(cr, uid, ides, {'numero_id' : ids[0]})
     
    def send(self, cr, uid, ids, context=None):
        obj_articulo= self.pool.get('articulo')
        articulos = obj_articulo.search(cr, uid, [('numero_id', '=', ids[0])])
        obj_articulo.write(cr, uid, articulos, { 'state' : 'impress',  })
        self.write(cr, uid, ids, { 'state' : 'a_publicar',  })
        
        
         
    def vote(self, cr, uid, ids, context=None):
        self.write(cr, uid, ids, { 'state' : 'voted'})
         
    def close(self, cr, uid, ids, context=None):
        self.write(cr, uid, ids, { 'state' : 'closed'})
            
       
       
        
numero()