describe('main page', () => {
    beforeEach(() => {
        cy.visit('http://localhost:5173/')
    });

    it('The h1 contains the correct text', () => {
        cy.get('h1').contains("Find the Best Flights for Your Journey!")
    })

    it('The filtering options are visible', () => {
        cy.get('.filter').should('have.length', 5)
    })

    it('The filtering options are clickable', () => {
        cy.get('.filter').each((filter) => {
            cy.wrap(filter).click()
        })
    })

    it('The search button is visible', () => {
        cy.get('.src-button').contains('Search')
    })

    it('The search button is clickable', () => {
        cy.get('.src-button').click()
    })

    it('The search returns results', () => {
        cy.get('.filter').first().click()
        cy.get('.dropdown div').should('be.visible').first().click({ force: true })
        cy.get('.src-button').click()
        cy.get('.flight').should('have.length.above', 0)
    })

    it('The flights are visible', () => {
        cy.get('.flight').should('have.length', 6)
    })

    it('The flights are clickable', () => {
        cy.get('.flight').first().click()
    })
})

describe('flight page', () => {
    beforeEach(() => {
        cy.visit('http://localhost:5173/')
        cy.get('.flight').first().click()
    });

    it('The filters are visible', () => {
        cy.get('.filter').should('have.length', 6)
    })

    it('The filters are clickable', () => {
        cy.get('.filter').each((filter) => {
            cy.wrap(filter).click()
        });
    })

    it('The search button is visible', () => {
        cy.get('.src-button').contains('Search')
    })

    it('The search button is clickable', () => {
        cy.get('.src-button').click()
    })

    it('The seat plan is visible', () => {
        cy.get('.seat-plan').should('be.visible')
    })

    it('A seat is already selected', () => {
        cy.visit('http://localhost:5173/')
        cy.get('.flight').first().click()
        cy.get('.selected-seats').should('be.visible')
        cy.get('.selected-seats ul li').should('have.length.at.least', 1)
        cy.get('.total-price h3').invoke('text').then((text) => {
            const price = parseFloat(text.replace(/[^0-9.]/g, '')) // Extract numeric value
            expect(price).to.be.greaterThan(0)
        })
    })

    it('A seat is clickable', () => {
        cy.get('.seat:not(.booked-seat)').eq(0).click()
        cy.get('.seat:not(.booked-seat)').eq(1).click()
        cy.get('.selected-seats ul li').should('have.length.at.least', 2)
    })

    it('Booking seats is showing alert', () => {
        cy.get('.seat:not(.booked-seat)').eq(0).click()
        cy.get('.seat:not(.booked-seat)').eq(1).click()

        cy.intercept('PUT', 'http://localhost:8080/api/seats/book', {
            statusCode: 200,
        }).as('mockBookSeats')

        cy.get('button').contains('Book Seats').click()

        cy.wait('@mockBookSeats')

        cy.on('window:alert', (text) => {
            expect(text).to.match(/Seats .* booked successfully!/)
        })

        cy.get('.selected-seats ul li').should('have.length', 0)
    })
})