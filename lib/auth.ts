import {cookies} from 'next/headers';
import {firestore} from './firebase-admin';

const COOKIE='radius_admin';

export async function login(username:string,password:string){
  const snap=await firestore().collection('admins').where('username','==',username).limit(1).get();
  if(snap.empty)return false;
  const admin=snap.docs[0].data();
  if(admin.status==='disabled')return false;
  const configuredPassword=process.env.ADMIN_PASSWORD;
  if(!configuredPassword || password!==configuredPassword)return false;
  (await cookies()).set(COOKIE,JSON.stringify({id:snap.docs[0].id,username,role:admin.role||'admin'}),{httpOnly:true,secure:true,sameSite:'lax',path:'/'});
  return true;
}
export async function currentAdmin(){const c=(await cookies()).get(COOKIE)?.value;if(!c)return null;try{return JSON.parse(c)}catch{return null}}
export async function logout(){(await cookies()).delete(COOKIE)}
