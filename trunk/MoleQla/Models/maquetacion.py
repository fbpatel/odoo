from openerp.osv import fields, osv
from datetime import date

class maquetacion(osv.osv):
    
    _name = "maquetacion"
    _description = "Maquetacion"
    
    _columns = {       
        'articulo_id': fields.many2one('articulo','Articulo'), 
        'seccion_id': fields.many2one('seccion','Seccion'),
        'observaciones': fields.binary('Observaciones'),
        'filenameObv': fields.char('FilenameObv'),  
        'maquetador_id': fields.integer('Maquetador'),
        'state':fields.selection([('start', 'En Maquetacion'), ('send', 'Maquetado'), ('cancel', 'Rechazado')], 'Estado de la maquetacion'),
        'comentarios': fields.text('Comentarios'),
        'versiones_anteriores' : fields.one2many('articulo', 'old_maquetacion_id','Version anterior'),
        }
    
    _defaults = {
                  'state':'start',
                  'filenameObv':'observaciones.pdf'
                  }
    
    
    
    def aceptar(self, cr, uid, ids, context=None):
        maquetacion = self.browse(cr, uid, ids, context)
        articulo_obj = self.pool.get('articulo')
        d = fields.date.today()
        articulo_obj.write(cr, 1, maquetacion.articulo_id.id, { 'state' : 'published', 'fecha_maq':d})
        self.write(cr, uid, ids, { 'state' : 'send' })
        
        
    def rechazar(self, cr, uid, ids, context=None):
        maquetacion = self.browse(cr, uid, ids, context)
        self.write(cr, uid, ids, { 'state' : 'cancel'})
        articulo_obj = self.pool.get('articulo')
        articulo_id = articulo_obj.search(cr, uid, [('maquetacion_id', '=', maquetacion.id)])
        articulo = articulo_obj.browse(cr, uid, articulo_id, context)
        
        vals = {'seccion_id':maquetacion.seccion_id.id,
                'archivo':articulo.archivo,'filename':articulo.filename,'nombre':articulo.nombre,
                'tipo_articulo':articulo.tipo_articulo,'tipo_autor':articulo.tipo_autor,
                'palabras_clave':articulo.palabras_clave,'user_id':1,'old_maquetacion_id':maquetacion.id}
        articulo_obj.create(cr, 1, vals, context=None)
        
        articulo_obj.write(cr, 1, maquetacion.articulo_id.id, { 'state' : 'cancel_m' })
        
    
    
    
    
maquetacion()