import {Router} from 'express'
import {listUsers} from '../controllers/user.controller'
import {listPosts} from '../controllers/post.controller'
import {photoUser} from '../controllers/photo.controller'
import {createAudit,getAudits,updateAudits,deleteAudits,getAuditId,getAuditsB64,getAuditIdB64} from '../controllers/audit.controller'
const router = Router()
///VIEW USERS
router.get('/api/user',listUsers)
/**
 * @openapi
 * /api/user:
 *    get:
 *      tags:
 *        - Usuarios
 *      summary: "Listar usuarios"
 *      description: Lista de usuarios totales de la api externa, cada registro de las peticiones es almacenado en PG
 *      
 *      responses:
 *        '200':
 *          description: Retorna lista de usuarios y crea registro en la base de datos
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */
///VIEW POSTS
router.get('/api/post',listPosts)
/**
 * @openapi
 * /api/post:
 *    get:
 *      tags:
 *        - Publicaciones
 *      summary: "Listar publicaciones"
 *      description: Lista de publicaciones totales de la api externa, cada registro de las peticiones es almacenado en pg
 *      
 *      responses:
 *        '200':
 *          description: Retorna lista de publicaciones y crea registro en la base de datos
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */
///VIEW PHOTOS
router.get('/api/photo/:userId',photoUser)
/**
 * @openapi
 * /api/photo/{userId}:
 *    get:
 *      tags:
 *        - Fotos
 *      summary: "Listar fotos por usuario"
 *      description: Lista de fotos de la api externa  por usuario, cada registro de las peticiones es almacenado en pg
 *      parameters:
 *          - in: path
 *            name: userId
 *            required: true
 *            description: id Numeric required
 *            schema:
 *              type: integer
 *      responses:
 *        '200':
 *          description: Operación exitosa
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */



///VIEW AUDITS B64
router.get('/api/audit/base',getAuditsB64)
/**
 * @openapi
 * /api/audit/base:
 *    get:
 *      tags:
 *        - Regitros en Base64
 *      summary: "Listar registro de peticiones"
 *      description: Lista del registro historico de peticiones en formato base64
 *      responses:
 *        '200':
 *          description: Operación exitosa
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */


///GET AUDIT
router.get('/api/audit/base/:id',getAuditIdB64)

/**
 * @openapi
 * /api/audit/base/{id}:
 *    get:
 *      tags:
 *        - Regitros en Base64
 *      summary: "Consultar registro por id en formato base64"
 *      description: Consulta los registros por id en formato base 64
 *      parameters:
 *          - in: path
 *            name: id
 *            required: true
 *            description: id Numeric required
 *            schema:
 *              type: integer
 *      responses:
 *        '204':
 *          description: Operacion exitosa
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Se ha eliminado el registro exitosamente
 *      security:
 *       - bearerAuth: []
 */



///CREATE AUDIT
router.post('/api/audit/create',createAudit)
/**
 * @openapi
 * /api/audit/create:
 *    post:
 *      tags:
 *        - Regitros
 *      summary: "Crear un registro de petición"
 *      description: Crea un registro de peticion en PG
 *      requestBody:
 *          content:
 *            application/json:
 *              schema:
 *                $ref: "#/components/schemas/audit"
 *      responses:
 *        '200':
 *          description: Registro creado exitosamente
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */
///VIEW AUDITS
router.get('/api/audit',getAudits)
/**
 * @openapi
 * /api/audit:
 *    get:
 *      tags:
 *        - Regitros
 *      summary: "Listar registro de peticiones"
 *      description: Lista del registro historico de peticiones
 *      responses:
 *        '200':
 *          description: Operación exitosa
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */



///GET AUDIT
router.get('/api/audit/:id',getAuditId)

/**
 * @openapi
 * /api/audit/{id}:
 *    get:
 *      tags:
 *        - Regitros
 *      summary: "Consultar registro por id"
 *      description: Consulta los registros por id
 *      parameters:
 *          - in: path
 *            name: id
 *            required: true
 *            description: id Numeric required
 *            schema:
 *              type: integer
 *      responses:
 *        '204':
 *          description: Operacion exitosa
 *        '500':
 *          description: Error al realizar la consultax
 *      security:
 *       - bearerAuth: []
 */


///UPDATE AUDITS
router.put('/api/audit/:id',updateAudits)
/**
 * @openapi
 * /api/audit/{id}:
 *    put:
 *      tags:
 *        - Regitros
 *      summary: "Editar registro de petición por id"
 *      description: Edita los registros de peticiónes a la api externa
 *      requestBody:
 *          content:
 *            application/json:
 *              schema:
 *                $ref: "#/components/schemas/audit"
 *      parameters:
 *          - in: path
 *            name: id
 *            required: true
 *            description: id Numeric required
 *            schema:
 *              type: integer
 *      responses:
 *        '204':
 *          description: Se ha editado exitosamente
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Error al realizar la consulta
 *      security:
 *       - bearerAuth: []
 */


///DELETE AUDITS
router.delete('/api/audit/:id',deleteAudits)
/**
 * @openapi
 * /api/audit/{id}:
 *    delete:
 *      tags:
 *        - Regitros
 *      summary: "Eliminar registro de petición por id"
 *      description: Elimina los registros de peticiónes a la api externa
 *      parameters:
 *          - in: path
 *            name: id
 *            required: true
 *            description: id Numeric required
 *            schema:
 *              type: integer
 *      responses:
 *        '204':
 *          description: Se ha eliminado el registro exitosamente
 *        '500':
 *          description: Error al realizar la consulta
 *        '404':
 *          description: Se ha eliminado el registro exitosamente
 *      security:
 *       - bearerAuth: []
 */


export default router