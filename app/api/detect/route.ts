import {NextResponse} from 'next/server';
import {getPublicOrigin} from '@/lib/network';
export async function GET(req:Request){const origin=getPublicOrigin(req.headers);return NextResponse.json({origin,host:req.headers.get('x-forwarded-host')||req.headers.get('host')||null,protocol:req.headers.get('x-forwarded-proto')||'https'});}
