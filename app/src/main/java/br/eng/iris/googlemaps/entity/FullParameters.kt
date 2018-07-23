package br.eng.iris.googlemaps.entity

import br.eng.iris.googlemaps.infra.OperationMethod

data class FullParameters(
        val url: String,
        val method: OperationMethod
)