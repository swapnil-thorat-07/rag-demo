Reference Page used for the RAG Demo: - https://www.chewy.com/education

Analysis:
What the page structure actually looks like

From the page content:
-Top-level category sections: Dog, Cat, Bird, Fish, etc.
- Each section contains:
  - A header
  - A list of article titles (links)
- There are also:
  - Featured sections (e.g., “All Creatures Covered”)
  - Hero content / banners
  - Mixed editorial blocks

Conclusion:
👉 This is NOT a long article page
👉 It’s a hierarchical content hub

Brute Force:
Most RAG chunkers assume:
<big paragraph> → split by tokens

But this page is:
Category → List of short titles

If you chunk naively:
- Each chunk = tiny (bad embeddings)
- No semantic grouping
- Retrieval = garbage or empty

Right Approach:
Each chunk should look like:
Category: Dog
Articles:
- How Long Are Dogs Pregnant?
- Dog Breeds Prone to Anxiety
- Why Do Dogs Like To Carry Sticks?

👉 This creates:
- meaningful context
- better embeddings
- retrievable units