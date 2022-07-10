import {DataSource} from 'typeorm'
import {Audit} from './entities/Audit'


//////DATABASE CONFIGURATION
//////REMEMBER IT IS NECESSARY TO CREATE THE DATABASE BEFORE PERFORMING THE FOLLOWING CONFIGURATION
//////FILL IN THE CONSTANTS WITH YOUR DATABASE CONFIGURATION

const HOSTDB = "localhost"  
const PORTDB = 5432 
const USERDB = "postgres" ///USERNAME ASSIGNED TO THE DATABASE
const PWDB = "admin"  ///PASSWORD ASSIGNED TO THE DATABASE
const DB = "i4digital"   ///DATABASE NAME



export const AppDataSource = new DataSource({
    type: "postgres",
    host: HOSTDB,
    port: PORTDB,
    username: USERDB,
    password: PWDB,
    database: DB,
    entities: [Audit],
    logging: true,
    synchronize: true,
    // subscribers: [],
    // migrations: [],
})