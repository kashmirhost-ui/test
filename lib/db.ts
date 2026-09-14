import {firestore} from './firebase-admin';
export async function collectionDocs<T=any>(name:string){const snap=await firestore().collection(name).get();return snap.docs.map(d=>({id:d.id,...d.data()})) as T[];}
export async function collectionCount(name:string){const snap=await firestore().collection(name).count().get();return snap.data().count;}
export async function addDoc(name:string,data:any){const ref=await firestore().collection(name).add({...data,createdAt:new Date().toISOString()});return ref.id;}
export async function setDoc(name:string,id:string,data:any){await firestore().collection(name).doc(id).set(data,{merge:true});}
