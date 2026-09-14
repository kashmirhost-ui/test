import Shell from '@/components/Shell';
import {collectionCount} from '@/lib/db';
import {currentAdmin} from '@/lib/auth';
export const dynamic='force-dynamic';
export default async function Page(){const admin=await currentAdmin();if(!admin)return <Shell><div className="card"><h1>Sign in required</h1><a href="/login">Go to login</a></div></Shell>;let data=[0,0,0,0];let error='';try{data=await Promise.all(['users','sessions','nas','auth_logs'].map(x=>collectionCount(x)))}catch(e:any){error=e?.message||'Firebase unavailable';}const labels=['Users','Online Sessions','NAS','Auth Logs'];return <Shell><div className="toolbar"><h1>Dashboard</h1></div>{error?<div className="notice">{error}. Configure Firebase in Render.</div>:<div className="grid">{data.map((n,i)=><div className="card" key={i}><h3>{labels[i]}</h3><div className="stat">{n}</div></div>)}</div>}</Shell>}
