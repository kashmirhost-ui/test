import {getApps,initializeApp,cert} from 'firebase-admin/app';import {getFirestore} from 'firebase-admin/firestore';
function adminApp(){if(getApps().length)return getApps()[0];const json=process.env.FIREBASE_SERVICE_ACCOUNT_JSON;if(json)return initializeApp({credential:cert(JSON.parse(json))});return initializeApp();}
export const firestore=()=>getFirestore(adminApp());
