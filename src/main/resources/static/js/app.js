'use strict';

const BASE = 'http://localhost:8080/api/v1/CircuitoX/api/celulares';

const URLS = {
  crear:      `${BASE}/crear`,
  buscar:     (m) => `${BASE}/buscar/${encodeURIComponent(m)}`,
  actualizar: `${BASE}/actualizar`,
  eliminar:   (m) => `${BASE}/eliminar/${encodeURIComponent(m)}`,
  sumar:      `${BASE}/sumar-stock`,
  restar:     `${BASE}/restar-stock`,
  listar:     `${BASE}/listar`,
};

const $  = (id) => document.getElementById(id);
const val = (id) => $(id).value.trim();

function showResult(id, msg, type) {
  $(id).innerHTML = `
    <div class="result ${type}">
      <div class="result-tag">${type === 'ok' ? 'Respuesta' : 'Error'}</div>
      <div class="result-msg">${msg.replace(/</g, '&lt;')}</div>
    </div>`;
}

function setBtn(id, loading) {
  const btn = $(id);
  btn.disabled = loading;
  if (loading) { btn._txt = btn.textContent; btn.textContent = 'Cargando…'; }
  else btn.textContent = btn._txt;
}

async function checkBackend() {
  const label = $('statusLabel');
  try {
    await fetch(URLS.listar, { signal: AbortSignal.timeout(3000) });
    label.textContent = '● Backend online';
    label.className = 'status online';
  } catch {
    label.textContent = '● Backend offline';
    label.className = 'status offline';
  }
}

function initTabs() {
  document.querySelectorAll('.tab').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.tab').forEach(b => b.classList.remove('active'));
      document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
      btn.classList.add('active');
      document.getElementById(`tab-${btn.dataset.tab}`).classList.add('active');
    });
  });
}

$('btnListar').addEventListener('click', async () => {
  setBtn('btnListar', true);
  try {
    const res  = await fetch(URLS.listar);
    const data = await res.json();
    if (!res.ok) { showResult('listResult', data.message || `Error ${res.status}`, 'err'); return; }
    renderTable('listResult', data.message || '');
  } catch (e) {
    showResult('listResult', 'No se pudo conectar con el backend.', 'err');
  } finally {
    setBtn('btnListar', false);
  }
});

function renderTable(id, message) {
  const raw = message.replace(/^Lista de celulares:\s*/, '').trim();
  const matches = [...raw.matchAll(/\{([^}]+)\}/g)];

  if (matches.length === 0) {
    $(id).innerHTML = raw === '[]' || raw === ''
      ? '<div class="empty">No hay dispositivos registrados.</div>'
      : `<div class="result ok"><div class="result-tag">Respuesta</div><div class="result-msg">${message}</div></div>`;
    return;
  }

  const devices = matches.map(m => {
    const obj = {};
    m[1].split(',').forEach(pair => {
      const [k, ...rest] = pair.split('=');
      if (k) obj[k.trim()] = rest.join('=').trim();
    });
    return obj;
  });

  const rows = devices.map(d => {
    const stock = parseInt(d.stock || 0, 10);
    const cls = stock === 0 ? 'badge-zero' : stock < 5 ? 'badge-low' : 'badge-ok';
    return `<tr>
      <td>${d.brand || '—'}</td>
      <td>${d.model || '—'}</td>
      <td>${d.storage || '—'}</td>
      <td>${d.ram || '—'} GB</td>
      <td>${d.color || '—'}</td>
      <td>$${d.price || '0'}</td>
      <td><span class="badge ${cls}">${stock}</span></td>
    </tr>`;
  }).join('');

  $(id).innerHTML = `
    <div class="table-wrap">
      <table>
        <thead><tr>
          <th>Marca</th><th>Modelo</th><th>Almacenamiento</th>
          <th>RAM</th><th>Color</th><th>Precio</th><th>Stock</th>
        </tr></thead>
        <tbody>${rows}</tbody>
      </table>
    </div>`;
}

$('createForm').addEventListener('submit', async () => {
  const brand = val('c_brand'), model = val('c_model');
  if (!brand || !model) { showResult('createResult', 'Marca y Modelo son obligatorios.', 'err'); return; }

  const body = {
    brand, model,
    storage: val('c_storage'),
    ram:     parseInt(val('c_ram'), 10)    || 0,
    color:   val('c_color'),
    price:   parseFloat(val('c_price'))    || 0,
    stock:   parseInt(val('c_stock'), 10)  || 0,
  };

  setBtn('btnCrear', true);
  try {
    const res  = await fetch(URLS.crear, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    const data = await res.json();
    showResult('createResult', data.message || `Error ${res.status}`, res.ok ? 'ok' : 'err');
    if (res.ok) $('createForm').reset();
  } catch (e) {
    showResult('createResult', 'Error de conexión.', 'err');
  } finally {
    setBtn('btnCrear', false);
  }
});

$('btnBuscar').addEventListener('click', async () => {
  const model = val('s_model');
  if (!model) { showResult('searchResult', 'Ingresa un modelo.', 'err'); return; }

  setBtn('btnBuscar', true);
  try {
    const res  = await fetch(URLS.buscar(model));
    const data = await res.json();
    const notFound = (data.message || '').toLowerCase().includes('no encontrado');
    showResult('searchResult', data.message || `Error ${res.status}`, notFound ? 'err' : 'ok');
  } catch (e) {
    showResult('searchResult', 'Error de conexión.', 'err');
  } finally {
    setBtn('btnBuscar', false);
  }
});

$('s_model').addEventListener('keydown', e => { if (e.key === 'Enter') $('btnBuscar').click(); });

$('updateForm').addEventListener('submit', async () => {
  const model = val('u_model');
  if (!model) { showResult('updateResult', 'El Modelo es obligatorio.', 'err'); return; }

  const body = {
    brand:   val('u_brand'),
    model,
    storage: val('u_storage'),
    ram:     parseInt(val('u_ram'), 10)    || 0,
    color:   val('u_color'),
    price:   parseFloat(val('u_price'))    || 0,
    stock:   parseInt(val('u_stock'), 10)  || 0,
  };

  setBtn('btnActualizar', true);
  try {
    const res  = await fetch(URLS.actualizar, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    const data = await res.json();
    const notFound = (data.message || '').toLowerCase().includes('no encontrado');
    showResult('updateResult', data.message || `Error ${res.status}`, (!res.ok || notFound) ? 'err' : 'ok');
  } catch (e) {
    showResult('updateResult', 'Error de conexión.', 'err');
  } finally {
    setBtn('btnActualizar', false);
  }
});

$('btnEliminar').addEventListener('click', async () => {
  const model = val('d_model');
  if (!model) { showResult('deleteResult', 'Ingresa un modelo.', 'err'); return; }
  if (!confirm(`¿Eliminar "${model}"? Esta acción no se puede deshacer.`)) return;

  setBtn('btnEliminar', true);
  try {
    const res  = await fetch(URLS.eliminar(model), { method: 'DELETE' });
    const data = await res.json();
    const notFound = (data.message || '').toLowerCase().includes('no encontrado');
    showResult('deleteResult', data.message || `Error ${res.status}`, (!res.ok || notFound) ? 'err' : 'ok');
    if (res.ok && !notFound) $('d_model').value = '';
  } catch (e) {
    showResult('deleteResult', 'Error de conexión.', 'err');
  } finally {
    setBtn('btnEliminar', false);
  }
});

$('d_model').addEventListener('keydown', e => { if (e.key === 'Enter') $('btnEliminar').click(); });

$('btnSumar').addEventListener('click', async () => {
  const model = val('add_model');
  const stock = parseInt(val('add_stock'), 10);
  if (!model || !stock || stock < 1) { showResult('addResult', 'Ingresa un modelo y una cantidad válida.', 'err'); return; }

  setBtn('btnSumar', true);
  try {
    const res  = await fetch(`${URLS.sumar}?model=${encodeURIComponent(model)}&stock=${stock}`, { method: 'PUT' });
    const data = await res.json();
    const notFound = (data.message || '').toLowerCase().includes('no encontrado');
    showResult('addResult', data.message || `Error ${res.status}`, (!res.ok || notFound) ? 'err' : 'ok');
    if (res.ok && !notFound) $('add_stock').value = '';
  } catch (e) {
    showResult('addResult', 'Error de conexión.', 'err');
  } finally {
    setBtn('btnSumar', false);
  }
});

$('btnRestar').addEventListener('click', async () => {
  const model = val('sub_model');
  const stock = parseInt(val('sub_stock'), 10);
  if (!model || !stock || stock < 1) { showResult('subResult', 'Ingresa un modelo y una cantidad válida.', 'err'); return; }

  setBtn('btnRestar', true);
  try {
    const res  = await fetch(`${URLS.restar}?model=${encodeURIComponent(model)}&stock=${stock}`, { method: 'PUT' });
    const data = await res.json();
    const notFound  = (data.message || '').toLowerCase().includes('no encontrado');
    const lowStock  = (data.message || '').toLowerCase().includes('suficiente');
    showResult('subResult', data.message || `Error ${res.status}`, (!res.ok || notFound || lowStock) ? 'err' : 'ok');
    if (res.ok && !notFound && !lowStock) $('sub_stock').value = '';
  } catch (e) {
    showResult('subResult', 'Error de conexión.', 'err');
  } finally {
    setBtn('btnRestar', false);
  }
});

document.addEventListener('DOMContentLoaded', () => {
  initTabs();
  checkBackend();
});