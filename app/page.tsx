import {redirect} from 'next/navigation';
import {currentAdmin} from '@/lib/auth';
export default async function Home(){redirect((await currentAdmin())?'/dashboard':'/login')}
