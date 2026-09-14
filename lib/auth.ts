import bcrypt from 'bcryptjs';
import {cookies} from 'next/headers';
import {one} from './db';
const COOKIE='radius_admin';
export async function login(username:string,password:string){
  const admin=await one<any>('SELECT id,username,password_hash,role FROM admins WHERE username=? LIMIT 1',[username]);
  if(!admin || !(await bcrypt.compare(password,admin.password_hash))) return false;
  (await cookies()).set(COOKIE,JSON.stringify({id:admin.id,username:admin.username,role:admin.role}),{httpOnly:true,secure:true,sameSite:'lax',path:'/'});
  return true;
}
export async function currentAdmin(){const c=(await cookies()).get(COOKIE)?.value;if(!c)return null;try{return JSON.parse(c)}catch{return null}}
export async function logout(){(await cookies()).delete(COOKIE)}
