export function getPublicOrigin(headers:Headers){const forwarded=headers.get('x-forwarded-proto');const host=headers.get('x-forwarded-host')||headers.get('host');if(!host) return null;return `${forwarded||'https'}://${host}`;}

export function normalizeHost(value:string){return value.trim().replace(/^https?:\/\//,'').replace(/\/+$/,'');}
