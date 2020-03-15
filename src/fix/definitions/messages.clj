(ns fix.definitions.messages)

(def messages {:RequestForPositionsAck {:category "app", :type "AO", :definitions 111, :ordering 111}, :SecurityStatus {:category "app", :type "f", :definitions 111, :ordering 111}, :DerivativeSecurityListUpdateReport {:category "app", :type "BR", :definitions 111, :ordering 111}, :TradingSessionStatus {:category "app", :type "h", :definitions 111, :ordering 111}, :QuoteStatusRequest {:category "app", :type "a", :definitions 111, :ordering 111}, :SecurityListRequest {:category "app", :type "x", :definitions 111, :ordering 111}, :OrderMassActionRequest {:category "app", :type "CA", :definitions 111, :ordering 111}, :CollateralRequest {:category "app", :type "AX", :definitions 111, :ordering 111}, :AllocationInstruction {:category "app", :type "J", :definitions 111, :ordering 111}, :DontKnowTrade {:category "app", :type "Q", :definitions 111, :ordering 111}, :MarketDataRequestReject {:category "app", :type "Y", :definitions 111, :ordering 111}, :AllocationInstructionAck {:category "app", :type "P", :definitions 111, :ordering 111}, :OrderStatusRequest {:category "app", :type "H", :definitions 111, :ordering 111}, :AllocationReportAck {:category "app", :type "AT", :definitions 111, :ordering 111}, :CrossOrderCancelRequest {:category "app", :type "u", :definitions 111, :ordering 111}, :ResendRequest {:category "admin", :type "2", :definitions 111, :ordering 111}, :CollateralAssignment {:category "app", :type "AY", :definitions 111, :ordering 111}, :ConfirmationAck {:category "app", :type "AU", :definitions 111, :ordering 111}, :ApplicationMessageReport {:category "app", :type "BY", :definitions 111, :ordering 111}, :BidResponse {:category "app", :type "l", :definitions 111, :ordering 111}, :Advertisement {:category "app", :type "7", :definitions 111, :ordering 111}, :CollateralResponse {:category "app", :type "AZ", :definitions 111, :ordering 111}, :CollateralInquiry {:category "app", :type "BB", :definitions 111, :ordering 111}, :ListExecute {:category "app", :type "L", :definitions 111, :ordering 111}, :PositionMaintenanceRequest {:category "app", :type "AL", :definitions 111, :ordering 111}, :MarketDefinitionRequest {:category "app", :type "BT", :definitions 111, :ordering 111}, :SecurityDefinitionRequest {:category "app", :type "c", :definitions 111, :ordering 111}, :MarketDataIncrementalRefresh {:category "app", :type "X", :definitions 111, :ordering 111}, :OrderCancelReplaceRequest {:category "app", :type "G", :definitions 111, :ordering 111}, :MarketDefinitionUpdateReport {:category "app", :type "BV", :definitions 111, :ordering 111}, :PositionReport {:category "app", :type "AP", :definitions 111, :ordering 111}, :AllocationInstructionAlert {:category "app", :type "BM", :definitions 111, :ordering 111}, :TradingSessionListRequest {:category "app", :type "BI", :definitions 111, :ordering 111}, :ContraryIntentionReport {:category "app", :type "BO", :definitions 111, :ordering 111}, :Logout {:category "admin", :type "5", :definitions 111, :ordering 111}, :TradingSessionStatusRequest {:category "app", :type "g", :definitions 111, :ordering 111}, :OrderMassCancelRequest {:category "app", :type "q", :definitions 111, :ordering 111}, :SettlementObligationReport {:category "app", :type "BQ", :definitions 111, :ordering 111}, :Email {:category "app", :type "C", :definitions 111, :ordering 111}, :StreamAssignmentReport {:category "app", :type "CD", :definitions 111, :ordering 111}, :NewOrderSingle {:category "app", :type "D", :definitions 111, :ordering 111}, :Reject {:category "admin", :type "3", :definitions 111, :ordering 111}, :NewOrderMultileg {:category "app", :type "AB", :definitions 111, :ordering 111}, :TradeCaptureReportRequest {:category "app", :type "AD", :definitions 111, :ordering 111}, :OrderMassActionReport {:category "app", :type "BZ", :definitions 111, :ordering 111}, :TradeCaptureReport {:category "app", :type "AE", :definitions 111, :ordering 111}, :MarketDefinition {:category "app", :type "BU", :definitions 111, :ordering 111}, :Quote {:category "app", :type "S", :definitions 111, :ordering 111}, :SecurityStatusRequest {:category "app", :type "e", :definitions 111, :ordering 111}, :TestRequest {:category "admin", :type "1", :definitions 111, :ordering 111}, :QuoteStatusReport {:category "app", :type "AI", :definitions 111, :ordering 111}, :SecurityTypeRequest {:category "app", :type "v", :definitions 111, :ordering 111}, :TradingSessionList {:category "app", :type "BJ", :definitions 111, :ordering 111}, :NewOrderCross {:category "app", :type "s", :definitions 111, :ordering 111}, :UserRequest {:category "app", :type "BE", :definitions 111, :ordering 111}, :CollateralInquiryAck {:category "app", :type "BG", :definitions 111, :ordering 111}, :SettlementInstructions {:category "app", :type "T", :definitions 111, :ordering 111}, :Logon {:category "admin", :type "A", :definitions 111, :ordering 111}, :OrderMassStatusRequest {:category "app", :type "AF", :definitions 111, :ordering 111}, :ExecutionAcknowledgement {:category "app", :type "BN", :definitions 111, :ordering 111}, :DerivativeSecurityListRequest {:category "app", :type "z", :definitions 111, :ordering 111}, :UserResponse {:category "app", :type "BF", :definitions 111, :ordering 111}, :ListStatus {:category "app", :type "N", :definitions 111, :ordering 111}, :NetworkCounterpartySystemStatusResponse {:category "app", :type "BD", :definitions 111, :ordering 111}, :PositionMaintenanceReport {:category "app", :type "AM", :definitions 111, :ordering 111}, :QuoteRequest {:category "app", :type "R", :definitions 111, :ordering 111}, :BusinessMessageReject {:category "app", :type "j", :definitions 111, :ordering 111}, :News {:category "app", :type "B", :definitions 111, :ordering 111}, :Confirmation {:category "app", :type "AK", :definitions 111, :ordering 111}, :CrossOrderCancelReplaceRequest {:category "app", :type "t", :definitions 111, :ordering 111}, :RFQRequest {:category "app", :type "AH", :definitions 111, :ordering 111}, :ApplicationMessageRequest {:category "app", :type "BW", :definitions 111, :ordering 111}, :BidRequest {:category "app", :type "k", :definitions 111, :ordering 111}, :IOI {:category "app", :type "6", :definitions 111, :ordering 111}, :ExecutionReport {:category "app", :type "8", :definitions 111, :ordering 111}, :SecurityList {:category "app", :type "y", :definitions 111, :ordering 111}, :SecurityListUpdateReport {:category "app", :type "BK", :definitions 111, :ordering 111}, :OrderCancelReject {:category "app", :type "9", :definitions 111, :ordering 111}, :MultilegOrderCancelReplace {:category "app", :type "AC", :definitions 111, :ordering 111}, :MarketDataSnapshotFullRefresh {:category "app", :type "W", :definitions 111, :ordering 111}, :SettlementInstructionRequest {:category "app", :type "AV", :definitions 111, :ordering 111}, :Heartbeat {:category "admin", :type "0", :definitions 111, :ordering 111}, :RegistrationInstructions {:category "app", :type "o", :definitions 111, :ordering 111}, :DerivativeSecurityList {:category "app", :type "AA", :definitions 111, :ordering 111}, :ListStrikePrice {:category "app", :type "m", :definitions 111, :ordering 111}, :SecurityDefinitionUpdateReport {:category "app", :type "BP", :definitions 111, :ordering 111}, :QuoteRequestReject {:category "app", :type "AG", :definitions 111, :ordering 111}, :UserNotification {:category "app", :type "CB", :definitions 111, :ordering 111}, :NetworkCounterpartySystemStatusRequest {:category "app", :type "BC", :definitions 111, :ordering 111}, :TradeCaptureReportAck {:category "app", :type "AR", :definitions 111, :ordering 111}, :SequenceReset {:category "admin", :type "4", :definitions 111, :ordering 111}, :SecurityDefinition {:category "app", :type "d", :definitions 111, :ordering 111}, :TradeCaptureReportRequestAck {:category "app", :type "AQ", :definitions 111, :ordering 111}, :MarketDataRequest {:category "app", :type "V", :definitions 111, :ordering 111}, :StreamAssignmentRequest {:category "app", :type "CC", :definitions 111, :ordering 111}, :OrderCancelRequest {:category "app", :type "F", :definitions 111, :ordering 111}, :StreamAssignmentReportACK {:category "app", :type "CE", :definitions 111, :ordering 111}, :QuoteResponse {:category "app", :type "AJ", :definitions 111, :ordering 111}, :SecurityTypes {:category "app", :type "w", :definitions 111, :ordering 111}, :RequestForPositions {:category "app", :type "AN", :definitions 111, :ordering 111}, :AssignmentReport {:category "app", :type "AW", :definitions 111, :ordering 111}, :RegistrationInstructionsResponse {:category "app", :type "p", :definitions 111, :ordering 111}, :OrderMassCancelReport {:category "app", :type "r", :definitions 111, :ordering 111}, :MassQuote {:category "app", :type "i", :definitions 111, :ordering 111}, :AllocationReport {:category "app", :type "AS", :definitions 111, :ordering 111}, :AdjustedPositionReport {:category "app", :type "BL", :definitions 111, :ordering 111}, :MassQuoteAcknowledgement {:category "app", :type "b", :definitions 111, :ordering 111}, :CollateralReport {:category "app", :type "BA", :definitions 111, :ordering 111}, :ListStatusRequest {:category "app", :type "M", :definitions 111, :ordering 111}, :TradingSessionListUpdateReport {:category "app", :type "BS", :definitions 111, :ordering 111}, :ConfirmationRequest {:category "app", :type "BH", :definitions 111, :ordering 111}, :ListCancelRequest {:category "app", :type "K", :definitions 111, :ordering 111}, :NewOrderList {:category "app", :type "E", :definitions 111, :ordering 111}, :ApplicationMessageRequestAck {:category "app", :type "BX", :definitions 111, :ordering 111}, :QuoteCancel {:category "app", :type "Z", :definitions 111, :ordering 111}})