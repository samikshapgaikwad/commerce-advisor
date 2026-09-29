# AI-Powered Reactive Commerce Advisor

An intelligent commerce system that detects business signals, generates AI-powered recommendations, and requires human approval before applying changes.

## Project Overview

This system monitors product inventory and demand patterns to detect business signals such as low stock or demand spikes. When a signal is detected, it generates appropriate recommendations (pricing or reorder) using either rule-based or AI-powered strategies. All recommendations require human approval before being applied to the live product data.

## Architecture

The system consists of:
- **Spring Boot 3.x backend** with Java 21
- **React 18 + Vite frontend**
- **H2 in-memory database** for development
- **Event-driven asynchronous processing**
- **AI integration** with LiteLLM gateway
## Backend Setup

1. Navigate to the project root directory
2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
## Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
## Environment Variables

The application uses the following environment variables:

### Backend Configuration
```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# Commerce Engine Strategy Configuration
commerce.pricing.strategy=rule  # or 'ai'
commerce.reorder.strategy=rule  # or 'ai'

# LLM Configuration
llm.api.url=https://litellm-qc.zycus.net/v1/chat/completions
llm.api.key=sk-SfyNGxhcv7RnQKbZFWX2LQ
## API Endpoints

### Products
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Orders
- `POST /api/orders/simulate` - Simulate an order (triggers business signals)

### Inventory
- `GET /api/inventory/{productId}` - Get inventory history for a product

### Suggestions
## Demo Instructions

1. Start the backend server
2. Start the frontend development server
3. Open http://localhost:5173 in your browser
4. You should see two demo products:
   - PRD-003 Organic Cotton T-Shirt (stock: 8, threshold: 15)
   - PRD-008 Hoodie (stock: 11, threshold: 12)

5. Click "Simulate Sale" on either product
6. If the sale reduces stock below the threshold, an INVENTORY_LOW signal will be triggered
7. An agentic workflow will run asynchronously to generate reorder suggestions
8. The suggestion will appear in the "Reorder Suggestions" section
9. Click "Accept" to apply the suggested reorder quantity to the product's stock
## Strategy Switching

The system supports runtime strategy switching without restart:

### To switch to AI pricing strategy:
```bash
curl -X POST http://localhost:8080/api/config/pricing-strategy/ai
```

### To switch to rule-based pricing strategy:
```bash
curl -X POST http://localhost:8080/api/config/pricing-strategy/rule
```

### To switch to AI reorder strategy:
```bash
## Agentic Flow

The system implements an event-driven agentic loop:

1. **Business Signal Detection** - Inventory changes trigger signals
2. **Asynchronous Processing** - Events are processed in the background
3. **Recommendation Generation** - AI or rule-based strategies create suggestions
## AI Integration

The system integrates with LiteLLM gateway for AI-powered recommendations:

### INVENTORY_LOW Prompt Context
- Product details (SKU, name, category, price, stock, threshold, demand velocity)
- Emphasis on merchandising tradeoffs between preserving inventory and maintaining sales

### DEMAND_SPIKE Prompt Context
- Demand velocity changes
- Current inventory levels
- Pricing context
- Product/category information

### Response Validation
All AI responses are validated for:
## Seed Data

The system includes demo products:

- **PRD-003 Organic Cotton T-Shirt**
  - Stock: 8
  - Reorder Threshold: 15
  - Demand Velocity: 12
  - Current Status: LOW_STOCK

## Extensibility Points

The system is designed for future enhancements:

- Additional business triggers
- New strategy implementations
- Enhanced AI prompting
- Supplier integration (currently placeholder fields)
- Cost/margin optimization

## Limitations

Features explicitly excluded per requirements:
- Competitor scraping
- Automated purchase orders
- Supplier APIs
- Storefront/checkout
- Customer-facing pricing display
- Authentication/authorization
- **PRD-008 Hoodie**
  - Stock: 11
  - Reorder Threshold: 12
  - Demand Velocity: 15
  - Current Status: LOW_STOCK
- Reasonable price/quantity ranges
- Valid confidence scores (0-1)
- Non-empty reasoning
- Proper directional guidance

Fallback to rule-based strategies occurs on AI failures.
4. **Human Approval** - Recommendations remain pending until approved
5. **Domain Effect** - Only approved recommendations modify live product data
curl -X POST http://localhost:8080/api/config/reorder-strategy/ai
```

### To switch to rule-based reorder strategy:
```bash
curl -X POST http://localhost:8080/api/config/reorder-strategy/rule
```
10. Click "Reject" to discard the suggestion
- `GET /api/suggestions/pricing` - Get pending pricing suggestions
- `GET /api/suggestions/reorder` - Get pending reorder suggestions
- `POST /api/suggestions/pricing/{id}/accept` - Accept pricing suggestion
- `POST /api/suggestions/pricing/{id}/reject` - Reject pricing suggestion
- `POST /api/suggestions/reorder/{id}/accept` - Accept reorder suggestion
- `POST /api/suggestions/reorder/{id}/reject` - Reject reorder suggestion
llm.api.model=qwen-cursor
```

### Frontend Configuration
The frontend uses Vite's proxy configuration to forward API requests to the backend.
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```
   mvn spring-boot:run
   ```
   
   Or run the jar directly:
   ```bash
   java -jar target/commerce-advisor-0.0.1-SNAPSHOT.jar
   ```
- **Strategy pattern** for flexible recommendation engines

## Prerequisites

- Java 21
- Maven 3.8+
- Node.js 16+
- npm or yarn