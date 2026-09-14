import Shell from '@/components/Shell';
import {collectionDocs} from '@/lib/db';
import {currentAdmin} from '@/lib/auth';
export const dynamic='force-dynamic';
export default async function Page(){const admin=await currentAdmin();if(!admin)return <Shell><div className="card"><h1>Sign in required</h1><a href="/login">Go to login</a></div></Shell>;let data:any[]=[];let error='';try{data=await collectionDocs('nas')}catch(e:any){error=e?.message||'Firebase unavailable'}return <Shell><div className="toolbar"><h1>NAS / Devices</h1></div>{error?<div className="notice">{error}. Configure Firebase in Render.</div>:<div className="card"><div style={{overflowX:'auto'}}><table className="table"><thead><tr>{data[0]?Object.keys(data[0]).map(k=><th key={k}>{k}</th>):null}</tr></thead><tbody>{data.map((r:any,i:number)=><tr key={i}>{Object.keys(r).map(k=><td key={k}>{String(r[k]??'')}</td>)}</tr>)}</tbody></table></div></div>}</Shell>}
