import express from 'express'
import morgan from 'morgan'
import cors from 'cors'
import Routes from './routes/module.routes'

import swaggerUi  from 'swagger-ui-express'
import SwaggerSetup from './docs/swagger'

const app = express()
app.use(cors({
    origin: '*',
    credentials:true,            //access-control-allow-credentials:true
}))
app.use(morgan('dev'))

app.use(express.json())
app.use(Routes)
app.use("/docs",swaggerUi.serve,swaggerUi.setup(SwaggerSetup))
export default app