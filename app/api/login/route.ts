import {NextResponse} from 'next/server';
import {login} from '@/lib/auth';
export async function POST(req:Request){const {username,password}=await req.json();const ok=await login(String(username||''),String(password||''));return NextResponse.json({ok},{status:ok?200:401});}
