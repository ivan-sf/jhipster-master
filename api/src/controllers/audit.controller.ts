import { Request, Response } from "express"
import {Audit} from '../entities/Audit'
import request from 'request'

export const createAudit = async (req:Request,res:Response) =>{
    try{
        const {method,status,type,response} = req.body
        const audit = new Audit()
        audit.method = method
        audit.status = status
        audit.type = type
        audit.response = response
        await audit.save()
        console.log({"req":req.body})
        res.send({"req":req.body})
    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}

export const getAudits = async (req:Request, res:Response)=>{
    try{
        const audits = await Audit.find()
        return res.json(audits)
    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}

export const getAuditsB64 = async (req:Request, res:Response)=>{
    try{
        const audits:any = await Audit.find()
        return res.json(btoa(JSON.stringify(audits)))
    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}


export const updateAudits = async (req:Request, res:Response)=>{
    try{
        const {id} = req.params
        let audit = await Audit.findOneBy({id:Number(id)})
    
        if(!audit) return res.status(404).json({"error":"audit not found"})
    
        Audit.update({id: Number(id)},req.body)
        // const json:any = {"method":req.method,"status":204,"type":`updated audit ${id}` }
        
        // request({
        //     url: 'http://localhost:4000/api/audit/create',
        //     method: "POST",
        //     headers: {
        //         "content-type": "application/json",
        //         },
        //     json: json
        //     }, function (error, resp, body) {
        //         if(!error)
        //             return res.sendStatus(204)
        //         else
        //             return res.status(500).json({error})
        //     }
        // )
        return res.sendStatus(204)

    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}

export const getAuditId = async (req:Request, res:Response)=>{
    try{
        const {id} = req.params

        let audit = await Audit.findOneBy({id:Number(id)})
        if(!audit) return res.status(404).json({"error":"audit not found"})

        console.log(audit)
        return res.json(audit)

    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}

export const getAuditIdB64 = async (req:Request, res:Response)=>{
    try{
        const {id} = req.params
        let audit = await Audit.findOneBy({id:Number(id)})
        if(!audit) return res.status(404).json({"error":"audit not found"})

        console.log(audit)
        return res.json(btoa(JSON.stringify(audit)))

    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}

export const deleteAudits = async (req:Request, res:Response)=>{
    try{
        const {id} = req.params
        let audit = await Audit.delete({id:Number(id)})
    
        if(!audit) return res.status(404).json({"error":"audit not found"})
    
        const result = await Audit.delete({id: Number(id)})
        console.log(result)

        // const json:any = {"method":req.method,"status":204,"type":`deleted audit ${id}` }
        
        // request({
        //     url: 'http://localhost:4000/api/audit/create',
        //     method: "POST",
        //     headers: {
        //         "content-type": "application/json",
        //         },
        //     json: json
        //     }, function (error, resp, body) {
        //         if(!error)
        //             return res.sendStatus(204)
        //         else
        //             return res.status(500).json({error})
        //     }
        // )
        if(!result.affected)
            return res.status(204).send("deleted audit")
        else
            return res.sendStatus(204)

    }catch(error){
        if(error instanceof Error){
            res.status(500).json({"error":error.message})
        }
    }
}