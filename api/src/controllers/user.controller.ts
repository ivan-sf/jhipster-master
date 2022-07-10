import { Request, Response, Router } from "express"
import request from 'request'
import {AppSettings} from '../environment.dev'


export const listUsers = async (req:Request,res:Response) =>{

    request("https://jsonplaceholder.typicode.com/users",(err,response,body)=>{
        if (!err){
            const users:string = JSON.parse(body);
            const method:string = req.method
            const status:number = response.statusCode
            const type:string = "list users"
            const json:any = {"method":method,"status":status,"type":type,"response":body}
            request({
                url: `${AppSettings.API_ENDPOINT}audit/create`,
                method: "POST",
                headers: {
                    "content-type": "application/json",
                    },
                json: json
                }, function (error, resp, body) {
                    if(!error)
                        return res.status(200).send(users)
                    else
                        return res.status(500).json({error})
                }
            )
        }
    })
}
