import { Request, Response, Router } from "express"
import request from 'request'
import {AppSettings} from '../environment.dev'


export const photoUser = async (req:Request,res:Response) =>{
    const userId = req.params.userId

    request(`https://jsonplaceholder.typicode.com/albums?userId=${userId}`,(err,response,body)=>{
        if (!err){
            const albums:any = JSON.parse(body);
            const method:string = req.method
            const status:number = response.statusCode
            const type:string = `list photos user ${userId}`
            let photos:any = []
            let i = 0
            let length = albums.length
            // console.log("body.length--->",albums.length)
            if(albums.length == 0) return res.status(404).send("data not found")
            albums.forEach(function (element:any) {
                i++
                let idAlbum = element.id
                 request(`https://jsonplaceholder.typicode.com/photos?albumId=${idAlbum}`, (err,response,body)=>{
                    if(!err){
                        if(length == i){
                            i++
                            photos.push(JSON.parse(body));

                            setTimeout(() => {
                                const json:any = {"method":method,"status":status,"type":type,"response":JSON.stringify(photos)}
                                request({
                                    url: `${AppSettings.API_ENDPOINT}audit/create`,
                                    method: "POST",
                                    headers: {
                                        "content-type": "application/json",
                                        },
                                    json: json
                                    }, function (error, resp, body) {

                                        if(!error)
                                            return res.json(photos)
                                        else
                                            return res.status(500).json({error})
                                    }
                                )
                            }, 1500);
                        }
                        else{
                            photos.push(JSON.parse(body));
                        }
                    }else
                        return res.status(500).json({err})
                })
            })
        }
    })
}
