import app from './server'
import {AppDataSource} from './db'
import {AppSettings} from './environment.dev'

const port:number = AppSettings.PORT

async function main() {
    try{
        await AppDataSource.initialize();
        console.log("DB CONNECT")
        app.listen(port)
        console.log("server on port",port)
    }catch(error){
        console.log("error",error)
    }
}

main();