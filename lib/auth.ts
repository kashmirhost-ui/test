import {cookies} from 'next/headers';
import {firestore} from './firebase-admin';
const COOKIE='radius_admin';
function safeEqual(a:string,b:string){if(a.length!==b.length)return false;let x=0;for(let i=0;i<a.length;i++)x|=a.charCodeAt(i)^b.charCodeAt(i);return x===0;}
export async function login(username:string,password:string){
  const configuredUser=process.env.ADMIN_USERNAME||'admin';
  const configuredPassword=process.env.ADMIN_PASSWORD;
  if(configuredPassword && safeEqual(username,configuredUser) && safeEqual(password,configuredPassword)){
    (await cookies()).set(COOKIE,JSON.stringify({id:'env-admin',username,role:'superadmin'}),{httpOnly:true,secure:true,sameSite:'lax',path:'/',maxAge:60*60*12});return true;
  }
  try{
    const snap=await firestore().collection('admins').where('username','==',username).limit(1).get();
    if(snap.empty)return false;const admin=snap.docs[0].data();if(admin.status==='disabled')return false;
    if(typeof admin.password==='string' && safeEqual(password,admin.password)){
      (await cookies()).set(COOKIE,JSON.stringify({id:snap.docs[0].id,username,role:admin.role||'admin'}),{httpOnly:true,secure:true,sameSite:'lax',path:'/',maxAge:60*60*12});return true;
    }
  }catch{}
  return false;
}
export async function currentAdmin(){const c=(await cookies()).get(COOKIE)?.value;if(!c)return null;try{return JSON.parse(c)}catch{return null}}
export async function logout(){(await cookies()).delete(COOKIE)}
