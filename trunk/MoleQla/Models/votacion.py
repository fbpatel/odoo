# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv
from openerp.tools.translate import _

class votacion(osv.osv):
    
    _name = "votacion"
    _description = "Votacion"
    
    _columns = {    
        'numero_id' : fields.many2one('numero','Número'),  
        'user_id': fields.integer('Usuario'),
        'lineas_votacion_inv': fields.one2many('linea_votacion', 'votacion_inv_id','Artículos'), 
        'lineas_votacion_div': fields.one2many('linea_votacion', 'votacion_div_id','Artículos'),
        'state':fields.selection([('start', 'Pendiente'), ('send', 'Confirmado')], 'Estado del proceso'),
        }
    
    _defaults = {
        'state' : 'start'
                 }
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            destaque_name = record.numero_id[0].nombre
            
            
            res.append((record.id, destaque_name))
        return res
    
    def aceptar(self, cr, uid, ids, context=None):
        self.write(cr, 1, ids, { 'state' : 'send'})
        
    def write(self, cr, uid, ids, vals, context=None):
        puntos_inv=0
        puntos_div=0
        obj_linea_votacion = self.pool.get('linea_votacion')
        if 'lineas_votacion_div' in vals.keys():
            lineas_div = vals['lineas_votacion_div']
            
            for linea in lineas_div:
                
                if linea[2]!=False:
                    dic = linea[2]
                    puntos = dic['puntos']
                    if puntos <0:
                        raise osv.except_osv(_('Warning!'),_("No se puede puntuar de forma negativa."))
                else:
                    id_linea = linea[1]
                    puntos = obj_linea_votacion.browse(cr, 1, id_linea, context).puntos
                puntos_div = puntos_div + puntos
        if 'lineas_votacion_inv' in vals.keys():
            lineas_inv = vals['lineas_votacion_inv']
            
            for linea in lineas_inv:
                
                if linea[2]!=False:
                    dic = linea[2]
                    puntos = dic['puntos']
                    if puntos <0:
                        raise osv.except_osv(_('Warning!'),_("No se puede puntuar de forma negativa."))
                else:
                    id_linea = linea[1]
                    puntos = obj_linea_votacion.browse(cr, 1, id_linea, context).puntos
                puntos_inv = puntos_inv + puntos
            
        if (puntos_inv > 6) and (puntos_div > 6):
            raise osv.except_osv(_('Warning!'),_("El numero de maximo de puntos para repartir en un premio es de 6."))
        elif (puntos_inv > 6) and (puntos_div <= 6):
            raise osv.except_osv(_('Warning!'),_("El numero de maximo para de puntos repartir entre los articulos de investigacion es de 6."))
        elif (puntos_inv <= 6) and (puntos_div > 6):
            raise osv.except_osv(_('Warning!'),_("El numero de maximo para de puntos repartir entre los articulos divulgativos es de 6."))
        else:
            return super(votacion, self).write(cr, uid, ids, vals, context)
        
       
votacion()