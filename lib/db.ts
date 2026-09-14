import mysql from 'mysql2/promise';

let pool:mysql.Pool|undefined;
export function db(){
  if(!pool){
    const required=['DB_HOST','DB_USER','DB_NAME'];
    const missing=required.filter(k=>!process.env[k]);
    if(missing.length) throw new Error('Database is not configured. Missing: '+missing.join(', '));
    pool=mysql.createPool({host:process.env.DB_HOST,port:Number(process.env.DB_PORT||3306),user:process.env.DB_USER,password:process.env.DB_PASSWORD||'',database:process.env.DB_NAME,waitForConnections:true,connectionLimit:5});
  }
  return pool;
}
export async function query<T=any>(sql:string,params:any[]=[]):Promise<T[]> { const [rows]=await db().execute(sql,params); return rows as T[]; }
export async function one<T=any>(sql:string,params:any[]=[]):Promise<T|undefined>{const rows=await query<T>(sql,params);return rows[0];}
