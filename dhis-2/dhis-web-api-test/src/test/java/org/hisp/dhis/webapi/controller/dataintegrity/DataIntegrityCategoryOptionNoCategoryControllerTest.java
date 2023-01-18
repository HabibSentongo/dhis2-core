/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.webapi.controller.dataintegrity;

import static org.hisp.dhis.web.WebClientUtils.assertStatus;

import org.hisp.dhis.web.HttpStatus;
import org.junit.jupiter.api.Test;

class DataIntegrityCategoryOptionNoCategoryControllerTest extends AbstractDataIntegrityIntegrationTest
{
    private final String check = "category_options_no_categories";

    @Test
    void testCategoryOptionNoCategoriesExist()
    {

        String categoryOptionRed = assertStatus( HttpStatus.CREATED,
            POST( "/categoryOptions",
                "{ 'name': 'Red', 'shortName': 'Red' }" ) );

        assertNamedMetadataObjectExists( "categories", "default" );
        assertNamedMetadataObjectExists( "categoryOptions", "default" );
        /*
         * Note that the default category is implicit here, so the percentage
         * need to take that into account
         */
        assertHasDataIntegrityIssues( "categories", check, 50, categoryOptionRed, "Red", null,
            true );

    }

    @Test
    void testCategoryOptionsHaveCategories()
    {

        String categoryOptionRed = assertStatus( HttpStatus.CREATED,
            POST( "/categoryOptions",
                "{ 'name': 'Red', 'shortName': 'Red' }" ) );
        assertStatus( HttpStatus.CREATED,
            POST( "/categories",
                "{ 'name': 'Color', 'shortName': 'Color', 'dataDimensionType': 'DISAGGREGATION' ," +
                    "'categoryOptions' : [{'id' : '" + categoryOptionRed + "'} ] }" ) );

        assertHasNoDataIntegrityIssues( "categories", check, true );

    }

}