import { getApps, initializeApp } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';

const username = process.env.ADMIN_USERNAME || 'admin';
const password = process.env.ADMIN_PASSWORD;
if (!password) throw new Error('Set ADMIN_PASSWORD before running this script');
if (!getApps().length) initializeApp();
const db = getFirestore();
const ref = db.collection('admins').doc(username);
await ref.set({username, role:'superadmin', status:'active', password}, {merge:true});
console.log('Admin account created/updated:', username);
console.log('For production, replace the bootstrap password with Firebase Authentication credentials.');
