import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [products, setProducts] = useState([]);
  const [pricingSuggestions, setPricingSuggestions] = useState([]);
  const [reorderSuggestions, setReorderSuggestions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch data on component mount
  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [productsRes, pricingRes, reorderRes] = await Promise.all([
        axios.get('/api/products'),
        axios.get('/api/suggestions/pricing'),
        axios.get('/api/suggestions/reorder')
      ]);
      
      setProducts(productsRes.data);
      setPricingSuggestions(pricingRes.data);
      setReorderSuggestions(reorderRes.data);
    } catch (err) {
      setError('Failed to fetch data: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const simulateOrder = async (productId, quantity = 1) => {
    try {
      await axios.post('/api/orders/simulate', {
        productId,
        quantity
      });
      // Refresh data after order simulation
      fetchData();
    } catch (err) {
      setError('Failed to simulate order: ' + err.message);
    }
  };

  const acceptSuggestion = async (type, id) => {
    try {
      await axios.post(`/api/suggestions/${type}/${id}/accept`);
      // Refresh data after accepting suggestion
      fetchData();
    } catch (err) {
      setError('Failed to accept suggestion: ' + err.message);
    }
  };

  const rejectSuggestion = async (type, id) => {
    try {
      await axios.post(`/api/suggestions/${type}/${id}/reject`);
      // Refresh data after rejecting suggestion
      fetchData();
    } catch (err) {
      setError('Failed to reject suggestion: ' + err.message);
    }
  };

  if (loading) return <div className="app">Loading...</div>;
  if (error) return <div className="app error">{error}</div>;

  return (
    <div className="app">
      <header className="app-header">
        <h1>Commerce Advisor Dashboard</h1>
        <button onClick={fetchData}>Refresh Data</button>
      </header>

      <main className="app-main">
        {/* Products Section */}
        <section className="section">
          <h2>Products</h2>
          <div className="products-grid">
            {products.map(product => (
              <div key={product.id} className="product-card">
                <h3>{product.name}</h3>
                <p><strong>SKU:</strong> {product.sku}</p>
                <p><strong>Category:</strong> {product.category}</p>
                <p><strong>Price:</strong> ${product.currentPrice}</p>
                <p><strong>Stock:</strong> {product.stock}</p>
                <p><strong>Status:</strong> 
                  <span className={`status ${product.status.toLowerCase()}`}>
                    {product.status}
                  </span>
                </p>
                <p><strong>Demand Velocity:</strong> {product.demandVelocity}</p>
                <button 
                  onClick={() => simulateOrder(product.id, 1)}
                  className="btn btn-primary"
                >
                  Simulate Sale
                </button>
              </div>
            ))}
          </div>
        </section>

        {/* Pricing Suggestions Section */}
        <section className="section">
          <h2>Pricing Suggestions</h2>
          {pricingSuggestions.length === 0 ? (
            <p>No pending pricing suggestions</p>
          ) : (
            <div className="suggestions-grid">
              {pricingSuggestions.map(suggestion => (
                <div key={suggestion.id} className="suggestion-card pricing">
                  <h3>{suggestion.productName}</h3>
                  <p><strong>Trigger:</strong> 
                    <span className={`trigger-badge ${suggestion.trigger.toLowerCase()}`}>
                      {suggestion.trigger.replace('_', ' ')}
                    </span>
                  </p>
                  <p><strong>Recommended Price:</strong> ${suggestion.recommendedPrice}</p>
                  <p><strong>Direction:</strong> {suggestion.direction}</p>
                  <p><strong>Confidence:</strong> {(suggestion.confidence * 100).toFixed(1)}%</p>
                  <p><strong>Reasoning:</strong> {suggestion.reasoning}</p>
                  <div className="suggestion-actions">
                    <button 
                      onClick={() => acceptSuggestion('pricing', suggestion.id)}
                      className="btn btn-success"
                    >
                      Accept
                    </button>
                    <button 
                      onClick={() => rejectSuggestion('pricing', suggestion.id)}
                      className="btn btn-danger"
                    >
                      Reject
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>

        {/* Reorder Suggestions Section */}
        <section className="section">
          <h2>Reorder Suggestions</h2>
          {reorderSuggestions.length === 0 ? (
            <p>No pending reorder suggestions</p>
          ) : (
            <div className="suggestions-grid">
              {reorderSuggestions.map(suggestion => (
                <div key={suggestion.id} className="suggestion-card reorder">
                  <h3>{suggestion.productName}</h3>
                  <p><strong>Trigger:</strong> 
                    <span className={`trigger-badge ${suggestion.trigger.toLowerCase()}`}>
                      {suggestion.trigger.replace('_', ' ')}
                    </span>
                  </p>
                  <p><strong>Recommended Quantity:</strong> {suggestion.recommendedQuantity}</p>
                  <p><strong>Confidence:</strong> {(suggestion.confidence * 100).toFixed(1)}%</p>
                  <p><strong>Reasoning:</strong> {suggestion.reasoning}</p>
                  <div className="suggestion-actions">
                    <button 
                      onClick={() => acceptSuggestion('reorder', suggestion.id)}
                      className="btn btn-success"
                    >
                      Accept
                    </button>
                    <button 
                      onClick={() => rejectSuggestion('reorder', suggestion.id)}
                      className="btn btn-danger"
                    >
                      Reject
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;