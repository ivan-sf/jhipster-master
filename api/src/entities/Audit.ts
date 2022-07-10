import { Column, CreateDateColumn, Entity, PrimaryColumn, PrimaryGeneratedColumn, UpdateDateColumn, BaseEntity } from 'typeorm'

@Entity()

export class Audit extends BaseEntity{

    @PrimaryGeneratedColumn()
    id:number
    @Column()
    method:string
    @Column()
    status:number
    @Column()
    type:string
    @Column()
    response:string
    @CreateDateColumn()
    created_at:Date
    @UpdateDateColumn()
    update_ad:Date

}