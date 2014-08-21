# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class editor(osv.osv):
    
    _name = "editor"
    _description = "Editor"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'apellidos': fields.char('Apellidos', size=128),
        'descripcion': fields.text('Descripción'),
        'user_id': fields.many2one('res.users', 'Usuario'),
        'seccion_id': fields.many2one('seccion', 'Sección'),
        }
    
    def create(self, cr, uid, vals, context=None):
        id_res_users = vals['user_id']
                
        # Cogemos la id del grupo Editor Seccion
        cr.execute("SELECT id FROM res_groups WHERE name = 'Editor Seccion'")
        id_gr_editor = cr.fetchone()
        
        # Insertamos una contraseña por defecto
        cr.execute("UPDATE res_users SET password='password' WHERE id = " + str(id_res_users))
        
        # Insertamos al usuario en el grupo de editores
        cr.execute("INSERT INTO res_groups_users_rel(gid, uid) VALUES (" + str(id_gr_editor[0]) + "," + str(id_res_users) + ")")
        
        # Enviamos un correo al editor registrado con sus datos
        # Asunto y texto del email
        user_obj = self.pool.get('res.users')
        editor_id = user_obj.search(cr, uid, [('id', '=', id_res_users)])
        editor_regis = user_obj.browse(cr, uid, editor_id, context)
        email = editor_regis[0].login
        asunto = "Welcome to MoleQla "
        texto = "Your login details are: <br /> <ul> <li>User: "+email+"</li> <li>Password: password</li> </ul> <br /> <b>Please choose a password to access</b>"
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email, asunto, texto) 
        return super(editor, self).create(cr, uid, vals, context)     
    
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            editor_name = record.nombre
            
            
            res.append((record.id, editor_name))
        return res
editor()
