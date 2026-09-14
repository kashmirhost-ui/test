export default function Home() {
  return (
    <main style={{fontFamily:'system-ui',padding:40,maxWidth:900,margin:'0 auto'}}>
      <h1>WiFi RADIUS Manager</h1>
      <p>Next.js control panel is online.</p>
      <p>RADIUS management modules are being migrated from the legacy application.</p>
      <a href="/api/detect">Test automatic server detection</a>
    </main>
  );
}
