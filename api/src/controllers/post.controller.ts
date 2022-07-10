import { Request, Response, Router } from "express"
import request from 'request'
import {AppSettings} from '../environment.dev'


export const listPosts = async (req:Request,res:Response) =>{

    request("https://jsonplaceholder.typicode.com/posts",(err,response,body)=>{
        if (!err){
            const posts:string = JSON.parse(body);
            const method:string = req.method
            const status:number = response.statusCode
            const type:string = "list posts"
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
                        return res.send(posts)
                    else
                        return  res.status(500).json({error})
                }
            )
        }
    })
}
